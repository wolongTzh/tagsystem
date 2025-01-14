package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalLlmDetailMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.dao.mapper.LlmTaskMapper;
import com.tsinghua.tagsystem.model.LLMTaskScoreCalHelper;
import com.tsinghua.tagsystem.model.PathCollection;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LLMTaskServiceImpl implements LLMTaskService {

    @Autowired
    LlmTaskMapper llmTaskMapper;

    @Autowired
    EvalOverviewMapper evalOverviewMapper;

    @Autowired
    EvalLlmDetailMapper evalLlmDetailMapper;

    @Autowired
    DataInfoMapper dataInfoMapper;

    String llmTaskInterface;
    String llmCalculateInterface;
    String testDataPath;
    String compareLLMInterface;

    LLMTaskServiceImpl(AlchemistPathConfig config) {
        llmTaskInterface = config.getLlmTaskInterface();
        llmCalculateInterface = config.getLlmCalculateInterface();
        testDataPath = config.getTestDataPath();
        compareLLMInterface = config.getCompareLLMInterface();
    }

    @Override
    public int createLLMTask(CreateLLMTaskParam createLLMTaskParam) {
        LlmTask llmTask = LlmTask.builder()
                .llmCreateTime(LocalDateTime.now())
                .llmTaskName(createLLMTaskParam.getLlmTaskName())
                .llmCreateUserId(createLLMTaskParam.getEvalUserId())
                .llmCreateUserName(createLLMTaskParam.getEvalUserName())
                .llmType(createLLMTaskParam.getLlmType())
                .formatDescribe(createLLMTaskParam.getFormatDescribe())
                .llmScriptPath(createLLMTaskParam.getLlmScriptPath())
                .evalOverviewId(createLLMTaskParam.getEvalOverviewId())
                .llmInputPath(createLLMTaskParam.getLlmInputPath())
                .llmOutputPath("待开始")
                .systemDescribe(createLLMTaskParam.getSystemDescribe())
                .build();
        llmTaskMapper.insert(llmTask);
        return llmTask.getLlmTaskId();
    }

    @Override
    public LlmTaskDecorate display(int evalOverviewId) {
        LlmTaskDecorate llmTaskDecorate = new LlmTaskDecorate();
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        llmTaskDecorate.setEvalAlgoIds(evalOverview.getEvalAlgoIds());
        llmTaskDecorate.setEvalTestIds(evalOverview.getEvalTestIds());
        llmTaskDecorate.setEvalTestNames(evalOverview.getEvalTestNames());
        llmTaskDecorate.setEvalAlgoNames(evalOverview.getEvalAlgoNames());
        String modelListStr = evalOverview.getEvalAlgoIds();
        String testDataListStr = evalOverview.getEvalTestIds();
        if (!StringUtils.isEmpty(modelListStr)) {
            List<Integer> modelIdList = Arrays.stream(modelListStr.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<LlmTask> llmTaskList = llmTaskMapper.selectList(new QueryWrapper<LlmTask>().in("llm_task_id", modelIdList));
            for(LlmTask llmTask : llmTaskList) {
                if (llmTask.getLlmOutputPath().contains("开始时间")) {
                    double startTime = Double.parseDouble(llmTask.getLlmOutputPath().replace("开始时间", ""));
                    // 计算当前时间到开始时间的时间间隔
                    double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
                    String newContent = Math.floor(currentTime - startTime) + "s";
                    llmTask.setLlmOutputPath(newContent);
                }
            }
            llmTaskDecorate.setLlmTaskList(llmTaskList);
        }
        if (!StringUtils.isEmpty(testDataListStr)) {
            List<Integer> testDataIdList = Arrays.stream(testDataListStr.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<DataInfo> testDataList = dataInfoMapper.selectList(new QueryWrapper<DataInfo>().in("data_id", testDataIdList));
            llmTaskDecorate.setTestDataList(testDataList);
        }
        List<EvalLlmDetail> evalDetailList = evalLlmDetailMapper.selectList(new QueryWrapper<EvalLlmDetail>().eq("eval_overview_id", evalOverviewId));
        llmTaskDecorate.setEvalDetailList(evalDetailList);
        return llmTaskDecorate;
    }

    @Override
    public int deleteModel(int modelId, int evalOverviewId) {

        // 获取 EvalOverview 对象
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);

        // 获取 algoIds 和 algoNames，并转换为 ArrayList 方便操作
        List<String> algoIdList = new ArrayList<>(Arrays.asList(evalOverview.getEvalAlgoIds().split(",")));
        List<String> algoNameList = new ArrayList<>(Arrays.asList(evalOverview.getEvalAlgoNames().split(",")));

        // 找到 modelId 的索引并移除对应的元素
        int indexToRemove = algoIdList.indexOf(String.valueOf(modelId));
        if (indexToRemove != -1) {  // 如果找到该 modelId
            algoIdList.remove(indexToRemove);
            algoNameList.remove(indexToRemove);
        }

        // 将列表转换回逗号分隔的字符串
        String algoIds = String.join(",", algoIdList);
        String algoNames = String.join(",", algoNameList);

        // 更新 EvalOverview 对象
        evalOverview.setEvalAlgoIds(algoIds);
        evalOverview.setEvalAlgoNames(algoNames);

        // 打印结果（用于调试）
        System.out.println(algoIds);
        System.out.println(algoNames);
        evalLlmDetailMapper.delete(new QueryWrapper<EvalLlmDetail>().eq("llm_task_id", modelId).eq("eval_overview_id", evalOverviewId));
        evalOverviewMapper.updateById(evalOverview);
        llmTaskMapper.deleteById(modelId);
        return 1;
    }

    @Override
    public int deleteTestData(int testDataId, int evalOverviewId) {
        // 获取 EvalOverview 对象
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);

        // 获取 algoIds 和 algoNames，并转换为 ArrayList 方便操作
        List<String> testIdList = new ArrayList<>(Arrays.asList(evalOverview.getEvalTestIds().split(",")));
        List<String> testNameList = new ArrayList<>(Arrays.asList(evalOverview.getEvalTestNames().split(",")));

        // 找到 modelId 的索引并移除对应的元素
        int indexToRemove = testIdList.indexOf(String.valueOf(testDataId));
        if (indexToRemove != -1) {  // 如果找到该 modelId
            testIdList.remove(indexToRemove);
            testNameList.remove(indexToRemove);
        }

        // 将列表转换回逗号分隔的字符串
        String testIds = String.join(",", testIdList);
        String testNames = String.join(",", testNameList);

        // 更新 EvalOverview 对象
        evalOverview.setEvalTestIds(testIds);
        evalOverview.setEvalTestNames(testNames);

        // 打印结果（用于调试）
        System.out.println(testIds);
        System.out.println(testNames);
        evalLlmDetailMapper.delete(new QueryWrapper<EvalLlmDetail>().eq("eval_data_id", testDataId).eq("eval_overview_id", evalOverviewId));
        evalOverviewMapper.updateById(evalOverview);
        dataInfoMapper.deleteById(testDataId);
        return 1;
    }

    @Override
    public int runTask(StartLLMTaskParam param) throws IOException {
        System.out.println(JSON.toJSONString(param));
        EvalOverview evalOverview = evalOverviewMapper.selectById(param.getEvalOverviewId());
        LlmTask llmTask = llmTaskMapper.selectById(param.getLlmTaskId());
        param.setToken(evalOverview.getToken());
        param.setLlmTask(llmTask);
        String url = llmTaskInterface;
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }

    @Override
    public int updateStatus(LlmTask llmTask) {
        llmTaskMapper.updateById(llmTask);
        return 1;
    }

    @Override
    public int finishLLMTask(FinishLLMTaskParam param) throws IOException {
        LlmTask llmTask = llmTaskMapper.selectById(param.getLlmTaskId());
        llmTask.setLlmOutputPath(param.getLlmOutputPath());
        llmTaskMapper.updateById(llmTask);
        EvalOverview evalOverview = evalOverviewMapper.selectById(param.getEvalOverviewId());
        if(StringUtils.isEmpty(evalOverview.getEvalTestIds().split(","))) {
            return 1;
        }
        List<LLMTaskScoreCalHelper> llmTaskScoreCalHelperList = new ArrayList<>();
        List<Integer> testIdList = Arrays.stream(evalOverview.getEvalTestIds().split(","))
                .map(Integer::parseInt)   // 将每个字符串转换为整数
                .collect(Collectors.toList());  // 收集成一个列表
        for(Integer testId : testIdList){
            String dataPath = dataInfoMapper.selectById(testId).getDataPath();
            llmTaskScoreCalHelperList.add(LLMTaskScoreCalHelper.builder()
                            .llmTaskId(llmTask.getLlmTaskId())
                            .llmTaskName(llmTask.getLlmTaskName())
                            .testDataPath(dataPath)
                            .testDataId(testId)
                            .llmOutPutPath(llmTask.getLlmOutputPath())
                    .build());
        }
        String url = llmCalculateInterface;
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(llmTaskScoreCalHelperList));
        if(!StringUtils.isEmpty(param.getFee())) {
            String fee = "0.0";
            if(!StringUtils.isEmpty(evalOverview.getFee())) {
                fee = evalOverview.getFee();
            }
            evalOverview.setFee(String.valueOf(Double.parseDouble(fee) + Double.parseDouble(param.getFee())));
            evalOverviewMapper.updateById(evalOverview);
        }
        return 1;
    }

    @Override
    public int addLLMDetailRelation(List<AddLLMDetailRelationParam> params) {
        int evalOverviewId = llmTaskMapper.selectById(params.get(0).getLlmTaskId()).getEvalOverviewId();
        for(AddLLMDetailRelationParam param : params) {
            evalLlmDetailMapper.insert(EvalLlmDetail.builder()
                    .dataId(param.getTestDataId())
                    .llmTaskId(param.getLlmTaskId())
                    .evalLlmDetailTime(LocalDateTime.now())
                    .evalLlmDetailScore(param.getScore())
                    .evalOverviewId(evalOverviewId)
                    .build());
        }
        return 1;
    }

    @Override
    public int uploadTestData(UploadTestDataParam param) throws IOException {
        String dataName = param.getDataName();
        File destFile = new File(testDataPath + param.getEvalUserName() + "-" + dataName + ".json");
        MultipartFile multipartFile = param.getFile();
        multipartFile.transferTo(destFile);
        DataInfo dataInfo = new DataInfo();
        dataInfo.setDataName(dataName);
        dataInfo.setDataCreatorId(param.getEvalUserId());
        dataInfo.setDataCreatorName(param.getEvalUserName());
        dataInfo.setDataPath(testDataPath + param.getEvalUserName() + "-" + dataName + ".json");
        dataInfo.setDataGenTime(LocalDateTime.now());
        dataInfo.setDataType("EVAL");
        dataInfo.setDataRelaInfo("");
        dataInfoMapper.insert(dataInfo);

        List<LLMTaskScoreCalHelper> llmTaskScoreCalHelperList = new ArrayList<>();
        List<LlmTask> llmTaskList = llmTaskMapper.selectList(new QueryWrapper<LlmTask>().eq("eval_overview_id", param.getEvalOverviewId()));
        for(LlmTask llmTask : llmTaskList) {
            if(llmTask.getLlmOutputPath().contains("docker-pytorch-llm")) {
                llmTaskScoreCalHelperList.add(LLMTaskScoreCalHelper.builder()
                        .llmTaskId(llmTask.getLlmTaskId())
                        .llmTaskName(llmTask.getLlmTaskName())
                        .testDataPath(dataInfo.getDataPath())
                        .testDataId(dataInfo.getDataId())
                        .llmOutPutPath(llmTask.getLlmOutputPath())
                        .build());
            }
        }
        if(llmTaskScoreCalHelperList.size() > 0) {
            String url = llmCalculateInterface;
            HttpUtil.sendPostDataByJson(url, JSON.toJSONString(llmTaskScoreCalHelperList));
        }
        return dataInfo.getDataId();
    }

    @Override
    public String comparePredictAnswer(int modelId, int testDataId) throws IOException {
        String testDataPath = dataInfoMapper.selectById(testDataId).getDataPath();
        String modelResultPath = llmTaskMapper.selectById(modelId).getLlmOutputPath();
        String url = compareLLMInterface;
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(PathCollection.builder().modelResultPath(modelResultPath).testDataPath(testDataPath).build()));
        return "ner";
    }

    @Override
    public String genLog(int evalOverviewId) {
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        LocalDateTime initTime = evalOverview.getEvalOverviewTime();
        LocalDateTime now = LocalDateTime.now();
        long days = Duration.between(initTime, now).toDays();
        long hours = Duration.between(initTime, now).toHours() % 24;
        long minutes = Duration.between(initTime, now).toMinutes() % 60;
        String log = "任务" + evalOverview.getEvalOverviewName() + "开始于" + initTime + "<br>已进行" + days + "天" + hours + "小时" + minutes + "分钟";
        if(!StringUtils.isEmpty(evalOverview.getFee())) {
            log += "<br>所花费总额为：" + String.format("%.4f", Double.parseDouble(evalOverview.getFee())) + "元";
        }
        System.out.println(log);
        return log;
    }
}
