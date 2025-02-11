package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.DataInfoManager;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.*;
import com.tsinghua.tagsystem.model.VO.ManagerTasksVO;
import com.tsinghua.tagsystem.model.export.Entity;
import com.tsinghua.tagsystem.model.export.EntityValue;
import com.tsinghua.tagsystem.model.export.ExportRelation;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    TaskManager taskManager;

    @Autowired
    SubTaskManager subTaskManager;

    @Autowired
    WorkerTaskRelaManager workerTaskRelaManager;

    @Autowired
    DataInfoMapper dataInfoMapper;

    @Autowired
    EvalOverviewMapper evalOverviewMapper;

    String subTaskFile = "";
    String checkedFile = "";
    String escapedCheckFile = "";
    String finalFile = "";
    String taskBase = "";
    @Autowired
    public ManagerServiceImpl(AddressConfig addressConfig) {
        subTaskFile = addressConfig.getSubTaskFile();
        checkedFile = addressConfig.getChecked();
        escapedCheckFile = addressConfig.getEscapeCheck();
        finalFile = addressConfig.getFinalTag();
        taskBase = addressConfig.getTaskBase();
    }

    @Override
    @Transactional
    public void createTask(CreateTaskParam param) throws IOException {
        AlgoInput algoInput = JSONObject.parseObject(JSONObject.toJSONString(CommonUtil.readJsonOut(param.getFile())), AlgoInput.class);
        for(Relation relation : algoInput.getRelationList()) {
            relation.setPredicateOld(relation.getPredicate());
        }
        String taskId = UUID.randomUUID().toString();
        String filePath = String.format(finalFile, taskId);
        int relationNum = algoInput.getRelationNum();
        int uncheckedNum = algoInput.getRelationList().size();
        int totalNum = algoInput.getRelationList().size();
        String curTaskBase = String.format(taskBase, taskId);
        File dir = new File(curTaskBase);
        log.info("taskBase = " + curTaskBase);
        if(!dir.exists()) {
            log.info("enter create file!!!");
            dir.mkdir();
        }
        Task task = Task.builder()
                .taskId(taskId)
                .creatorId(param.getUserId())
                .title(param.getTitle())
                .status(TaskStateEnum.INIT.getContent())
                .filePath(filePath)
                .relationNum(relationNum)
                .uncheckedNum(uncheckedNum)
                .checkWorker(param.getMembers().getCheckingWorker().getName())
                .build();
        if(!StringUtils.isEmpty(param.getEvalOverviewId())) {
            task.setEvalOverviewId(param.getEvalOverviewId());
        }
        List<WorkerTaskRela> workerTaskRelaList = new ArrayList<>();
        List<SubTask> subTaskList = new ArrayList<>();
        for(List<TsUser> group : param.getMembers().getTaggingWorker()) {
            String subTaskId = UUID.randomUUID().toString();
            String subFilePath = String.format(subTaskFile, taskId, subTaskId);
            log.info("subFilePath = " + subFilePath);
            SubTask subTask = SubTask.builder()
                    .subTaskId(subTaskId)
                    .status(TaskStateEnum.INIT.getContent())
                    .filePath(subFilePath)
                    .parentId(taskId)
                    .build();
            int personNum = group.size();
            int base = totalNum / personNum;
            int remain = totalNum % personNum;
            int start = 0;
            int end = 0;
            File file = new File(subFilePath);
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(JSON.toJSONString(algoInput.getRelationList()));
            fileWriter.close();
            List<String> taggingWorkerNames = new ArrayList<>();
            for(TsUser user : group) {
                start = end;
                String relaId = UUID.randomUUID().toString();
                end += base;
                if(remain != 0) {
                    end++;
                    base--;
                }
                WorkerTaskRela workerTaskRela = WorkerTaskRela.builder()
                        .relaId(relaId)
                        .taskId(subTaskId)
                        .start(start)
                        .end(end)
                        .userId(user.getUserId())
                        .status(TaskStateEnum.INIT.getContent())
                        .untaggedNum(end - start)
                        .taskType("tag")
                        .build();
                workerTaskRelaList.add(workerTaskRela);
                taggingWorkerNames.add(user.getName());
            }
            subTask.setTagWorkers(String.join("，", taggingWorkerNames));
            subTaskList.add(subTask);
        }
        String relaId = UUID.randomUUID().toString();
        workerTaskRelaList.add(WorkerTaskRela.builder()
                .relaId(relaId)
                .taskId(taskId)
                .userId(param.getMembers().getCheckingWorker().getUserId())
                .status(TaskStateEnum.INIT.getContent())
                .start(0)
                .end(totalNum)
                .untaggedNum(totalNum)
                .taskType("check")
                .build());
        taskManager.save(task);
        workerTaskRelaManager.saveBatch(workerTaskRelaList);
        subTaskManager.saveBatch(subTaskList);
    }

    @Override
    public ManagerTasksVO getTasks(Integer userId) {
        List<ManagerTask> taskList = taskManager.getManagerTaskList(userId);
        List<SubTaskMsg> subTaskMsgList = new ArrayList<>();
        String taskId = "";
        List<ManagerTaskDTO> mmanagerTaskDTOList = new ArrayList<>();
        ManagerTaskDTO managerTaskDTO = new ManagerTaskDTO();
        for(ManagerTask managerTask : taskList) {
            if(!managerTask.getTaskId().equals(taskId)) {
                subTaskMsgList = new ArrayList<>();
                taskId = managerTask.getTaskId();
                managerTaskDTO = ManagerTaskDTO.builder()
                        .taskId(managerTask.getTaskId())
                        .relationNum(managerTask.getRelationNum())
                        .status(managerTask.getStatus())
                        .checkingWorker(managerTask.getCheckWorker())
                        .title(managerTask.getTitle())
                        .subTaskMsgs(subTaskMsgList)
                        .build();
                mmanagerTaskDTOList.add(managerTaskDTO);
            }
            subTaskMsgList.add(SubTaskMsg.builder()
                    .taskId(managerTask.getSubTaskId())
                    .status(managerTask.getSubTaskStatus())
                    .taggingWorker(managerTask.getTaggingWorker())
                    .build());
        }
        return ManagerTasksVO.builder()
                .managerTaskList(mmanagerTaskDTOList)
                .build();
    }

    @Override
    public ManagerTaskSupervise checkTask(String taskId) throws IOException {
        Task task = taskManager.getByTaskId(taskId);
        String path = task.getFilePath();
        File file = new File(path);
        List<Relation> taskList = new ArrayList<>();
        if(!file.exists()) {
            List<Relation> needNotCheckList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(String.format(escapedCheckFile, taskId))), Relation.class);
            List<CheckAtom> needCheckList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(String.format(checkedFile, taskId))), CheckAtom.class);
            for(CheckAtom checkAtom : needCheckList) {
                Relation relation = Relation.builder()
                        .id(checkAtom.getId())
                        .predicate(checkAtom.getPredicate())
                        .sourceStart(checkAtom.getSourceStart())
                        .sourceEnd(checkAtom.getSourceEnd())
                        .targetStart(checkAtom.getTargetStart())
                        .targetEnd(checkAtom.getTargetEnd())
                        .status(checkAtom.getStatus())
                        .text(checkAtom.getText())
                        .build();
                taskList.add(relation);
            }
            taskList.addAll(needNotCheckList);
        }
        else {
            taskList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(path)), Relation.class);
        }
        return ManagerTaskSupervise.builder()
                .taskList(taskList)
                .taskId(taskId)
                .build();
    }

    @Override
    public boolean saveCheck(ManagerTaskSupervise managerTaskSupervise) throws IOException {
        Task task = taskManager.getByTaskId(managerTaskSupervise.getTaskId());
        String path = task.getFilePath();
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(managerTaskSupervise.getTaskList()));
        fileWriter.close();
        return true;
    }

    @Override
    public JSONArray exportTask(String taskId) throws IOException {
        Task task = taskManager.getByTaskId(taskId);
        String path = task.getFilePath();
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(path)), Relation.class);
        relationList = relationList.stream().sorted((o1, o2) ->  o1.getId() - o2.getId()).collect(Collectors.toList());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        int curId = -1;
        Set<Integer> alreadyCheck = new HashSet<>();
        JSONArray result = new JSONArray();
        for(Relation relation : relationList) {
            if(relation.getStatus().equals("FAILED")) {
                continue;
            }
            if(relation.getId() != curId) {
                if(curId != -1) {
                    jsonArray.add(jsonObject);
                }
                alreadyCheck = new HashSet<>();
                jsonObject = new JSONObject();
                result = new JSONArray();
                List<String> outText = new ArrayList<>();
                outText.add(relation.getText());
                jsonObject.put("data", outText);
                jsonObject.put("result", result);
                curId = relation.getId();
            }
            if(!alreadyCheck.contains(relation.getSource_id())) {
                alreadyCheck.add(relation.getSource_id());
                EntityValue entityValue = EntityValue.builder()
                        .start(relation.getSourceStart())
                        .end(relation.getSourceEnd())
                        .text(relation.getHead())
                        .lables(relation.getSource_type())
                        .build();
                Entity entity = Entity.builder()
                        .id(relation.getSource_id())
                        .value(entityValue)
                        .type("lables")
                        .build();
                result.add(entity);
            }
            if(!alreadyCheck.contains(relation.getTarget_id())) {
                alreadyCheck.add(relation.getTarget_id());
                EntityValue entityValue = EntityValue.builder()
                        .start(relation.getTargetStart())
                        .end(relation.getTargetEnd())
                        .text(relation.getTail())
                        .lables(relation.getTarget_type())
                        .build();
                Entity entity = Entity.builder()
                        .id(relation.getTarget_id())
                        .value(entityValue)
                        .type("lables")
                        .build();
                result.add(entity);
            }
            List<String> lables = new ArrayList<>();
            lables.add(relation.getPredicate());
            ExportRelation exportRelation = ExportRelation.builder()
                    .from_id(relation.getSource_id())
                    .to_id(relation.getTarget_id())
                    .lables(lables)
                    .type("relation")
                    .build();
            result.add(exportRelation);
        }
        jsonArray.add(jsonObject);
        if(!StringUtils.isEmpty(task.getEvalOverviewId())) {
            EvalOverview evalOverview = evalOverviewMapper.selectById(task.getEvalOverviewId());
            DataInfo dataInfo = dataInfoMapper.selectById(evalOverview.getEvalAutoBuildTestId());
            String allPath = dataInfo.getDataPath();
            // 使用 BufferedReader 读取文件内容
            BufferedReader reader = new BufferedReader(new FileReader(allPath));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);  // 拼接每一行
            }
            reader.close();
            // 将文件内容转换为 JSONArray
            JSONArray oldJsonArray = JSONArray.parseArray(jsonContent.toString());
            oldJsonArray.addAll(jsonArray);
            BufferedWriter writer = new BufferedWriter(new FileWriter(allPath));
            writer.write(oldJsonArray.toJSONString());
            return oldJsonArray;
        }
        return jsonArray;
    }
}
