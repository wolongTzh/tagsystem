package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.dao.entity.multi.WorkerTask;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.*;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.WorkerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    SubTaskManager subTaskManager;

    @Autowired
    WorkerTaskRelaManager workerTaskRelaManager;

    @Autowired
    TaskManager taskManager;

    String subTaskFile = "";
    String checkedFile = "";
    String escapedCheckFile = "";
    String finalFile = "";
    String taskBase = "";
    @Autowired
    public WorkerServiceImpl(AddressConfig addressConfig) {
        subTaskFile = addressConfig.getSubTaskFile();
        checkedFile = addressConfig.getChecked();
        escapedCheckFile = addressConfig.getEscapeCheck();
        finalFile = addressConfig.getFinalTag();
        taskBase = addressConfig.getTaskBase();
    }

    @Override
    public WorkerTasksVO getTasks(Integer userId) {
        List<WorkerTask> workerTaskList = workerTaskRelaManager.getTasksTag(userId);
        workerTaskList.addAll(workerTaskRelaManager.getTasksCheck(userId));
        List<WorkerTaskDTO> workerTaskDTOList = new ArrayList<>();
        for(WorkerTask workerTask : workerTaskList) {
            Integer taskType = workerTask.getTaskType().equals("tag") ? 0 : 1;
            Integer dataNum = workerTask.getEnd() - workerTask.getStart();
            workerTaskDTOList.add(WorkerTaskDTO.builder()
                    .taskId(workerTask.getTaskId())
                    .taskType(taskType)
                    .dataNum(dataNum)
                    .relationNum(workerTask.getRelationNum())
                    .status(workerTask.getStatus())
                    .title(workerTask.getTitle())
                    .untaggedNum(workerTask.getUntaggedNum())
                    .build());
        }
        return WorkerTasksVO.builder()
                .workerTasks(workerTaskDTOList)
                .build();
    }

    @Override
    public WorkerTaskMsg tagTask(String relaId) throws IOException {
        WorkerTask workerTask = workerTaskRelaManager.getTask(relaId);
        String curSubTaskFile = String.format(subTaskFile, workerTask.getTaskId(), workerTask.getSubTaskId());
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(curSubTaskFile)), Relation.class);
        WorkerTaskRela workerTaskRela = workerTaskRelaManager.getByRelaId(relaId);
        if(!workerTaskRela.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
            workerTaskRela.setStatus(TaskStateEnum.TAGGING.getContent());
            workerTaskRelaManager.updateByRelaId(workerTaskRela);
            SubTask subTask = subTaskManager.getByTaskId(workerTaskRela.getTaskId());
            if(!subTask.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
                subTask.setStatus(TaskStateEnum.TAGGING.getContent());
                subTaskManager.updateByTaskId(subTask);
                Task task = taskManager.getByTaskId(subTask.getParentId());
                if(!task.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
                    task.setStatus(TaskStateEnum.TAGGING.getContent());
                    taskManager.updateByTaskId(task);
                }
                WorkerTaskRela workerTaskCheck = workerTaskRelaManager.getByTaskId(task.getTaskId());
                if(!workerTaskCheck.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
                    workerTaskCheck.setStatus(TaskStateEnum.TAGGING.getContent());
                    workerTaskRelaManager.updateByRelaId(workerTaskCheck);
                }
            }
        }
        return WorkerTaskMsg.builder()
                .title(workerTask.getTitle())
                .relationNum(workerTask.getRelationNum())
                .taskId(relaId)
                .unTaggedNum(workerTask.getUntaggedNum())
                .relationList(relationList.subList(workerTaskRela.getStart(), workerTaskRela.getEnd()))
                .build();
    }

    @Override
    public boolean saveTag(WorkerTaskMsg param) throws IOException {
        if(param.getUnTaggedNum() == 0) {
            WorkerTaskRela workerTaskRela = workerTaskRelaManager.getByRelaId(param.getTaskId());
            workerTaskRela.setStatus(TaskStateEnum.FINISHED.getContent());
            workerTaskRelaManager.updateByRelaId(workerTaskRela);
            List<WorkerTaskRela> judgeGroupFinishList = workerTaskRelaManager.judgeGroupFinish(workerTaskRela.getTaskId());
            boolean groupFinish = true;
            for(WorkerTaskRela judge : judgeGroupFinishList) {
                if(!judge.getStatus().equals(TaskStateEnum.FINISHED.getContent())) {
                    groupFinish = false;
                    break;
                }
            }
            if(groupFinish) {
                SubTask subTask = subTaskManager.getByTaskId(workerTaskRela.getTaskId());
                subTask.setStatus(TaskStateEnum.FINISHED.getContent());
                subTaskManager.updateByTaskId(subTask);
                List<SubTask> judgeTaskFinishList = subTaskManager.judgeTaskFinish(subTask.getParentId());
                boolean taskFinish = true;
                for(SubTask judge : judgeTaskFinishList) {
                    if(!judge.getStatus().equals(TaskStateEnum.FINISHED.getContent())) {
                        taskFinish = false;
                        break;
                    }
                }
                if(taskFinish) {
                    Task task = taskManager.getByTaskId(subTask.getParentId());
                    task.setStatus(TaskStateEnum.UNCHECKED.getContent());
                    taskManager.updateByTaskId(task);
                    WorkerTaskRela workerTaskCheck = workerTaskRelaManager.getByTaskId(task.getTaskId());
                    workerTaskCheck.setStatus(TaskStateEnum.UNCHECKED.getContent());
                    workerTaskRelaManager.updateByRelaId(workerTaskCheck);
                }
            }
        }
        WorkerTask workerTask = workerTaskRelaManager.getTask(param.getTaskId());
        String curSubTaskFile = String.format(subTaskFile, workerTask.getTaskId(), workerTask.getSubTaskId());
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(curSubTaskFile)), Relation.class);
        List<Relation> headList = relationList.subList(0, workerTask.getStart());
        List<Relation> tailList = relationList.subList(workerTask.getEnd(), relationList.size());
        List<Relation> finalList = new ArrayList<>();
        finalList.addAll(headList);
        finalList.addAll(param.getRelationList());
        finalList.addAll(tailList);
        File file = new File(curSubTaskFile);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(finalList));
        fileWriter.close();
        WorkerTaskRela workerTaskRela = workerTaskRelaManager.getByRelaId(param.getTaskId());
        workerTaskRela.setUntaggedNum(param.getUnTaggedNum());
        workerTaskRelaManager.updateByRelaId(workerTaskRela);
        return true;
    }

    @Override
    public CheckTaskVO checkTask(String taskId) throws IOException {
        List<ManagerTask> managerTaskList = taskManager.getManagerCheckTaskList(taskId);
        List<CheckAtom> checkAtomList = new ArrayList<>();
        CheckTaskVO checkTaskVO = new CheckTaskVO();
        List<Relation> needNotCheck = new ArrayList<>();
        List<List<Relation>> allRelations = new ArrayList<>();
        boolean start = true;
        String parentTaskId = "";
        for(ManagerTask managerTask : managerTaskList) {
            List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(managerTask.getSubFilePath())), Relation.class);
            if(start) {
                parentTaskId = managerTask.getTaskId();
                File file = new File(String.format(checkedFile, parentTaskId));
                if(file.exists()) {
                    List<CheckAtom> checkAtoms = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(String.format(checkedFile, parentTaskId))), CheckAtom.class);
                    checkTaskVO.setCheckList(checkAtoms);
                    return checkTaskVO;
                }
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
            Relation relation = relations.get(0);
            if(judgeNeedCheck(relations)) {
                List<RelationSimple> relationSimpleList = new ArrayList<>();
                for(Relation r : relations) {
                    relationSimpleList.add(RelationSimple.builder()
                            .predicate(r.getPredicate())
                            .status(r.getStatus())
                            .build());
                }
                checkAtomList.add(CheckAtom.builder()
                        .text(relation.getText())
                        .relationList(relationSimpleList)
                        .id(relation.getId())
                        .sourceStart(relation.getSourceStart())
                        .sourceEnd(relation.getSourceEnd())
                        .targetStart(relation.getTargetStart())
                        .targetEnd(relation.getTargetEnd())
                        .predicate(relation.getPredicateOld())
                        .status(TaskStateEnum.UNCHECKED.getContent())
                        .build());
            }
            else {
                relation.setPredicateOld(null);
                needNotCheck.add(relation);
            }
        }
        File file = new File(String.format(escapedCheckFile, parentTaskId));
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(needNotCheck));
        fileWriter.close();
        file = new File(String.format(checkedFile, parentTaskId));
        fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(checkAtomList));
        fileWriter.close();
        checkTaskVO.setCheckList(checkAtomList);
        Task task = taskManager.getByTaskId(parentTaskId);
        if(!task.getStatus().equals(TaskStateEnum.CHECKING.getContent())) {
            task.setStatus(TaskStateEnum.CHECKING.getContent());
            taskManager.updateByTaskId(task);
            WorkerTaskRela workerTaskCheck = workerTaskRelaManager.getByRelaId(taskId);
            workerTaskCheck.setStatus(TaskStateEnum.CHECKING.getContent());
            workerTaskRelaManager.updateByRelaId(workerTaskCheck);
        }
        return checkTaskVO;
    }

    @Override
    public boolean saveCheck(SaveCheckParam param) throws IOException {
        WorkerTaskRela workerTaskRela = workerTaskRelaManager.getByRelaId(param.getTaskId());
        if(param.getUncheckedNum() == 0) {
            List<Relation> finalList = new ArrayList<>();
            List<Relation> needNotCheckList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(String.format(escapedCheckFile, workerTaskRela.getTaskId()))), Relation.class);
            for(CheckAtom checkAtom : param.getCheckList()) {
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
                finalList.add(relation);
            }
            finalList.addAll(needNotCheckList);
            finalList = finalList.stream().sorted((o1, o2) ->  o1.getId() - o2.getId()).collect(Collectors.toList());
            File file = new File(String.format(finalFile, workerTaskRela.getTaskId()));
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(JSON.toJSONString(finalList));
            fileWriter.close();
            WorkerTaskRela workerTaskCheck = workerTaskRelaManager.getByRelaId(param.getTaskId());
            workerTaskCheck.setUntaggedNum(param.getUncheckedNum());
            workerTaskCheck.setStatus(TaskStateEnum.FINISHED.getContent());
            workerTaskRelaManager.updateByRelaId(workerTaskCheck);
            Task task = taskManager.getByTaskId(workerTaskRela.getTaskId());
            task.setStatus(TaskStateEnum.FINISHED.getContent());
            taskManager.updateByTaskId(task);
        }
        else {
            WorkerTaskRela workerTaskCheck = workerTaskRelaManager.getByRelaId(param.getTaskId());
            workerTaskCheck.setUntaggedNum(param.getUncheckedNum());
            workerTaskRelaManager.updateByRelaId(workerTaskCheck);
        }
        File file = new File(String.format(checkedFile, workerTaskRela.getTaskId()));
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(param.getCheckList()));
        fileWriter.close();
        return true;
    }

    boolean judgeNeedCheck(List<Relation> relationList) {
        String curStatus = relationList.get(0).getStatus();
        String predicate = relationList.get(0).getPredicate();
        for(Relation relation : relationList) {
            if(!relation.getStatus().equals(curStatus)) {
                return true;
            }
            if(!relation.getPredicate().equals(predicate)) {
                return true;
            }
        }
        return false;
    }
}
