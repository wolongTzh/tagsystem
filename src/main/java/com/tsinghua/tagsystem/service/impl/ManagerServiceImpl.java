package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.AlgoInput;
import com.tsinghua.tagsystem.model.CheckAtom;
import com.tsinghua.tagsystem.model.Relation;
import com.tsinghua.tagsystem.model.SubTaskMsg;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.GetTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
        String taskId = UUID.randomUUID().toString();
        String filePath = String.format(finalFile, taskId);
        int relationNum = algoInput.getRelationNum();
        int uncheckedNum = algoInput.getRelationList().size();
        int totalNum = algoInput.getRelationList().size();
        taskBase = String.format(taskBase, taskId);
        File dir = new File(taskBase);
        if(!dir.exists()) {
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
                .build();
        List<WorkerTaskRela> workerTaskRelaList = new ArrayList<>();
        List<SubTask> subTaskList = new ArrayList<>();
        for(List<Integer> group : param.getMembers()) {
            String subTaskId = UUID.randomUUID().toString();
            String subFilePath = String.format(subTaskFile, taskId, subTaskId);
            SubTask subTask = SubTask.builder()
                    .subTaskId(subTaskId)
                    .status(TaskStateEnum.INIT.getContent())
                    .filePath(subFilePath)
                    .parentId(taskId)
                    .build();
            subTaskList.add(subTask);
            int personNum = group.size();
            int base = totalNum / personNum;
            int remain = totalNum % personNum;
            int start = 0;
            int end = 0;
            File file = new File(subFilePath);
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(JSON.toJSONString(algoInput.getRelationList()));
            fileWriter.close();
            for(Integer userId : group) {
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
                        .userId(userId)
                        .status(TaskStateEnum.INIT.getContent())
                        .untaggedNum(end - start)
                        .build();
                workerTaskRelaList.add(workerTaskRela);
            }
        }
        taskManager.save(task);
        workerTaskRelaManager.saveBatch(workerTaskRelaList);
        subTaskManager.saveBatch(subTaskList);
    }

    @Override
    public GetTasksVO getTasks(Integer userId) {
        List<ManagerTask> taskList = taskManager.getManagerTaskList(userId);
        GetTasksVO getTasksVO = new GetTasksVO();
        List<SubTaskMsg> subTaskMsgList = new ArrayList<>();
        boolean start = true;
        for(ManagerTask managerTask : taskList) {
            if(start) {
                getTasksVO = GetTasksVO.builder()
                        .taskId(managerTask.getTaskId())
                        .relationNum(managerTask.getRelationNum())
                        .status(managerTask.getStatus())
                        .title(managerTask.getTitle())
                        .build();
                start = false;
            }
            subTaskMsgList.add(SubTaskMsg.builder()
                    .taskId(managerTask.getSubTaskId())
                    .status(managerTask.getSubTaskStatus())
                    .build());
        }
        getTasksVO.setSubTaskMsgs(subTaskMsgList);
        return getTasksVO;
    }

    @Override
    public CheckTaskVO checkTask(String taskId) throws IOException {
        List<ManagerTask> managerTaskList = taskManager.getManagerCheckTaskList(taskId);
        List<CheckAtom> checkAtomList = new ArrayList<>();
        CheckTaskVO checkTaskVO = new CheckTaskVO();
        List<Relation> needNotCheck = new ArrayList<>();
        List<List<Relation>> allRelations = new ArrayList<>();
        boolean start = true;
        for(ManagerTask managerTask : managerTaskList) {
            List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(managerTask.getSubFilePath())), Relation.class);
            if(start) {
                checkTaskVO = CheckTaskVO.builder()
                        .taskId(managerTask.getTaskId())
                        .relationNum(managerTask.getRelationNum())
                        .title(managerTask.getTitle())
                        .build();
                for(Relation relation : relationList) {
                    List<Relation> newRelationlist = new ArrayList<>();
                    newRelationlist.add(relation);
                    allRelations.add(newRelationlist);
                }
                start = false;
                continue;
            }
            int index = 0;
            for(List<Relation> relations : allRelations) {
                relations.add(relationList.get(index));
                index++;
            }
        }
        for(List<Relation> relations : allRelations) {
            if(judgeNeedCheck(relations)) {
                checkAtomList.add(CheckAtom.builder()
                        .relationList(relations)
                        .status(TaskStateEnum.CHECKING.getContent())
                        .build());
            }
            else {
                needNotCheck.add(relations.get(0));
            }
        }
        File file = new File(String.format(escapedCheckFile, taskId));
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(needNotCheck));
        fileWriter.close();
        file = new File(String.format(checkedFile, taskId));
        fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(checkAtomList));
        fileWriter.close();
        checkTaskVO.setCheckList(checkAtomList);
        Task task = taskManager.getByTaskId(taskId);
        if(!task.getStatus().equals(TaskStateEnum.CHECKING.getContent())) {
            task.setStatus(TaskStateEnum.CHECKING.getContent());
            taskManager.updateByTaskId(task);
        }
        return checkTaskVO;
    }

    @Override
    public boolean saveCheck(SaveCheckParam param) throws IOException {
        if(param.getUncheckedNum() == 0) {
            List<Relation> finalList = new ArrayList<>();
            List<Relation> needNotCheckList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(String.format(escapedCheckFile, param.getTaskId()))), Relation.class);
            for(CheckAtom checkAtom : param.getCheckList()) {
                finalList.add(checkAtom.getRelationList().get(0));
            }
            finalList.addAll(needNotCheckList);
            finalList = finalList.stream().sorted((o1, o2) ->  o1.getId() - o2.getId()).collect(Collectors.toList());
            File file = new File(String.format(finalFile, param.getTaskId()));
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(JSON.toJSONString(finalList));
            fileWriter.close();
            Task task = taskManager.getByTaskId(param.getTaskId());
            task.setStatus(TaskStateEnum.FINISHED.getContent());
            taskManager.updateByTaskId(task);
        }
        else {
            File file = new File(String.format(checkedFile, param.getTaskId()));
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(JSON.toJSONString(param.getCheckList()));
            fileWriter.close();
        }
        return true;
    }

    boolean judgeNeedCheck(List<Relation> relationList) {
        String curStatus = relationList.get(0).getStatus();
        String predicate = relationList.get(0).getPredicate();
        for(Relation relation : relationList) {
            if(!relation.getStatus().equals(curStatus)) {
                return true;
            }
            if(curStatus.equals(TaskStateEnum.EDITED.getContent()) && !relation.getPredicate().equals(predicate)) {
                return true;
            }
        }
        return false;
    }
}
