package com.tsinghua.tagsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalLlmDetailMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.dao.mapper.LlmTaskMapper;
import com.tsinghua.tagsystem.model.params.CreateLLMTaskParam;
import com.tsinghua.tagsystem.service.LLMTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
}
