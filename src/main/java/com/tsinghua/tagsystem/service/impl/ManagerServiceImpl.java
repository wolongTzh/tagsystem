package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.*;
import com.tsinghua.tagsystem.model.VO.ManagerTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    TaskManager taskManager;

    @Autowired
    SubTaskManager subTaskManager;

    @Autowired
    WorkerTaskRelaManager workerTaskRelaManager;

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
}
