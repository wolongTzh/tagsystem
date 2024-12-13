package com.tsinghua.tagsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.model.params.CreateLLMTaskParam;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
}
