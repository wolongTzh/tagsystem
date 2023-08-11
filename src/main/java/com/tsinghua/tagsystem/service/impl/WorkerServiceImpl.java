package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.WorkerTask;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.Relation;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.WorkerTaskMsg;
import com.tsinghua.tagsystem.service.WorkerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<WorkerTask> workerTaskList = workerTaskRelaManager.getTasks(userId);
        return WorkerTasksVO.builder()
                .workerTasks(workerTaskList)
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
        }
        SubTask subTask = subTaskManager.getByTaskId(workerTaskRela.getTaskId());
        if(!subTask.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
            subTask.setStatus(TaskStateEnum.TAGGING.getContent());
            subTaskManager.updateByTaskId(subTask);
        }
        Task task = taskManager.getByTaskId(subTask.getParentId());
        if(!task.getStatus().equals(TaskStateEnum.TAGGING.getContent())) {
            task.setStatus(TaskStateEnum.TAGGING.getContent());
            taskManager.updateByTaskId(task);
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
                }
            }
        }
        WorkerTask workerTask = workerTaskRelaManager.getTask(param.getTaskId());
        String curSubTaskFile = String.format(subTaskFile, workerTask.getTaskId(), workerTask.getSubTaskId());
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray(curSubTaskFile)), Relation.class);
        List<Relation> headList = relationList.subList(0, param.getStart());
        List<Relation> tailList = relationList.subList(param.getEnd(), relationList.size());
        List<Relation> finalList = new ArrayList<>();
        finalList.addAll(headList);
        finalList.addAll(param.getRelationList());
        finalList.addAll(tailList);
        File file = new File(curSubTaskFile);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(JSON.toJSONString(finalList));
        fileWriter.close();
        return true;
    }
}
