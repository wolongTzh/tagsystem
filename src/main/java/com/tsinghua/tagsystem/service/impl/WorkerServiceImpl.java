package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.config.AddressConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.dao.entity.multi.WorkerTask;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.dao.mapper.ModelHelpTagMapper;
import com.tsinghua.tagsystem.enums.TaskStateEnum;
import com.tsinghua.tagsystem.manager.SubTaskManager;
import com.tsinghua.tagsystem.manager.TaskManager;
import com.tsinghua.tagsystem.manager.WorkerTaskRelaManager;
import com.tsinghua.tagsystem.model.*;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.export.Entity;
import com.tsinghua.tagsystem.model.export.EntityValue;
import com.tsinghua.tagsystem.model.export.ExportRelation;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.WorkerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    SubTaskManager subTaskManager;

    @Autowired
    WorkerTaskRelaManager workerTaskRelaManager;

    @Autowired
    TaskManager taskManager;

    @Autowired
    DataInfoMapper dataInfoMapper;

    @Autowired
    EvalOverviewMapper evalOverviewMapper;

    @Autowired
    ModelHelpTagMapper modelHelpTagMapper;

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
        System.out.println("relationList:");
        System.out.println(JSON.toJSONString(relationList));
        System.out.println("workerTask:");
        System.out.println(JSON.toJSONString(workerTask));
        Integer offset = relationList.size() - (workerTask.getEnd() - workerTask.getStart());
        System.out.println("offset:" + offset);
        workerTask.setEnd(workerTask.getEnd() + offset);
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
        workerTaskRela.setEnd(workerTask.getEnd());
        if(offset > 0) {
            Task task = taskManager.getByTaskId(workerTask.getTaskId());
            task.setRelationNum(task.getRelationNum() + offset);
            taskManager.updateByTaskId(task);
        }
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
            int[] needAddId = new int[relationList.size()];
            for(List<Relation> relations : allRelations) {
                Relation curRelation = relations.get(0);
                boolean findTag = false;
                int count = 0;
                for(Relation relation : relationList) {
                    if(relation.getText().equals(curRelation.getText()) && relation.getSourceStart() == curRelation.getSourceStart() && relation.getSourceEnd() == curRelation.getSourceEnd()) {
                        relations.add(relation);
                        findTag = true;
                        needAddId[count] = 1;
                        break;
                    }
                    count++;
                }
                if(!findTag) {
                    relations.add(Relation.builder().predicate("未标注").status("CHECKED").build());
                }
//                relations.add(relationList.get(index));
                index++;
            }
            for(int i = 0; i < needAddId.length; i++) {
                if(needAddId[i] == 0) {
                    List<Relation> newRelations = new ArrayList<>();
                    newRelations.add(Relation.builder().predicate("未标注").status("CHECKED").build());
                    newRelations.add(relationList.get(i));
                    allRelations.add(newRelations);
                }
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
                        .source_id(relation.getSource_id())
                        .source_type(relation.getSource_type())
                        .target_id(relation.getTarget_id())
                        .target_type(relation.getTarget_type())
                        .head(relation.getHead())
                        .tail(relation.getTail())
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
                        .source_id(checkAtom.getSource_id())
                        .source_type(checkAtom.getSource_type())
                        .target_id(checkAtom.getTarget_id())
                        .target_type(checkAtom.getTarget_type())
                        .head(checkAtom.getHead())
                        .tail(checkAtom.getTail())
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
            if(!StringUtils.isEmpty(task.getEvalOverviewId())) {
                updateTestData(task.getEvalOverviewId(), finalList);
            }
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

    void updateTestData(int evalOverviewId, List<Relation> finalList) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        int curId = -1;
        Set<Integer> alreadyCheck = new HashSet<>();
        JSONArray result = new JSONArray();
        for(Relation relation : finalList) {
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
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        DataInfo dataInfo = dataInfoMapper.selectById(evalOverview.getEvalAutoBuildTestId());
        String allPath = dataInfo.getDataPath();
        System.out.println("allPath");
        System.out.println(allPath);
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
        System.out.println("oldJsonArray");
        System.out.println(oldJsonArray.toJSONString());
        oldJsonArray.addAll(jsonArray);
        System.out.println("newJsonArray");
        System.out.println(oldJsonArray.toJSONString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(allPath));
        writer.write(oldJsonArray.toJSONString());
        writer.close();
        List<Map<String, String>> current = new java.util.ArrayList<>();
        Map<String, Integer> labelCountMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            JSONArray resultArray = jsonObject.getJSONArray("result");

            // 遍历 result 数组，找到 lables 字段
            for (int j = 0; j < resultArray.size(); j++) {
                JSONObject resultItem = resultArray.getJSONObject(j);
                if (resultItem.containsKey("lables")) {
                    // 获取 lables 字段
                    JSONArray labels = resultItem.getJSONArray("lables");
                    for (int k = 0; k < labels.size(); k++) {
                        String label = labels.getString(k);
                        // 统计标签出现次数
                        labelCountMap.put(label, labelCountMap.getOrDefault(label, 0) + 1);
                    }
                }
            }
        }

        // 将标签统计结果放到 List<Map<String, String>> 中
        for (Map.Entry<String, Integer> entry : labelCountMap.entrySet()) {
            Map<String, String> labelStat = new HashMap<>();
            labelStat.put("tagName", entry.getKey());
            labelStat.put("tagNum", String.valueOf(entry.getValue()));
            current.add(labelStat);
        }
        if(!StringUtils.isEmpty(dataInfo.getDataCurrentInfo())) {
            oldJsonArray = JSONArray.parseArray(dataInfo.getDataCurrentInfo());
            List<Map<String, String>> dataList = new ArrayList<>();
            for (int i = 0; i < oldJsonArray.size(); i++) {
                JSONObject j = oldJsonArray.getJSONObject(i);
                // 将 JSONObject 转换为 Map<String, String>
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("tagName", j.getString("tagName"));
                dataMap.put("tagNum", j.getString("tagNum"));
                dataList.add(dataMap);
            }
            mergeListToMap(dataList, current);
        }
        dataInfo.setDataCurrentInfo(JSONObject.toJSONString(current));
        dataInfoMapper.updateById(dataInfo);
        ModelHelpTag testHelpTag = modelHelpTagMapper.selectOne(new QueryWrapper<ModelHelpTag>().eq("eval_overview_id", evalOverviewId).eq("type", "test"));
        testHelpTag.setOutputPath(dataInfo.getDataPath());
        modelHelpTagMapper.updateById(testHelpTag);
    }

    void mergeListToMap(List<Map<String, String>> list, List<Map<String, String>> resultMap) {
        for (Map<String, String> item : list) {
            String tagName = item.get("tagName");
            String tagNum = item.get("tagNum");
            boolean findTag = false;
            for(Map<String, String> resultItem : resultMap) {
                if(resultItem.get("tagName").equals(tagName)) {
                    int newTagNum = Integer.parseInt(resultItem.get("tagNum")) + Integer.parseInt(tagNum);
                    resultItem.put("tagNum", String.valueOf(newTagNum));
                    findTag = true;
                }
            }
            if(!findTag) {
                resultMap.add(item);
            }
        }
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
