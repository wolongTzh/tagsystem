package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.model.LLMTaskScoreCalHelper;
import com.tsinghua.tagsystem.model.PathCollection;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.queue.MessageQueue;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.SftLLMService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SftLLMServiceImpl implements SftLLMService {

    @Autowired
    SftLlmMapper sftLlmMapper;

    @Autowired
    LlmTaskMapper llmTaskMapper;

    @Autowired
    EvalOverviewMapper evalOverviewMapper;

    @Autowired
    EvalLlmDetailMapper evalLlmDetailMapper;

    @Autowired
    DataInfoMapper dataInfoMapper;

    @Autowired
    OverviewDataRelationMapper overviewDataRelationMapper;

    @Autowired
    OverviewModelRelationMapper overviewModelRelationMapper;


    String sftTaskInterface;

    SftLLMServiceImpl(AlchemistPathConfig config) {
        sftTaskInterface = config.getSftTaskInterface();
    }

    @Override
    public int createSft(SftLlm sftLlm) {
        sftLlm.setCreateTime(LocalDateTime.now());
        sftLlmMapper.insert(sftLlm);
        return sftLlm.getSftId();
    }

    @Override
    public int runSft(SftLlm sftLlm) throws IOException {
        String url = sftTaskInterface;
        String ckptPath = HttpUtil.sendPostDataByJson(url, JSON.toJSONString(sftLlm));
        LlmTask llmTask = LlmTask.builder()
                .llmTaskName(sftLlm.getModelName())
                .llmCreateTime(sftLlm.getCreateTime())
                .llmCreateUserId(sftLlm.getUserId())
                .llmCreateUserName(sftLlm.getUserName())
                .llmType(ckptPath)
                .llmOutputPath("待开始")
                .formatDescribe(sftLlm.getSystemPrompt())
                .evalOverviewId(sftLlm.getOverviewId())
                .taskType("sft")
                .build();
        llmTaskMapper.insert(llmTask);
        EvalOverview evalOverview = evalOverviewMapper.selectById(sftLlm.getOverviewId());
        String algoIds = evalOverview.getEvalAlgoIds();
        String algoNames = evalOverview.getEvalAlgoNames();
        if(StringUtils.isEmpty(algoIds)) {
            algoIds = "" + llmTask.getLlmTaskId();
        }
        else {
            algoIds = algoIds + "," + llmTask.getLlmTaskId();
        }
        if(StringUtils.isEmpty(algoNames)) {
            algoNames = llmTask.getLlmTaskName();
        }
        else {
            algoNames = algoNames + "," + llmTask.getLlmTaskName();
        }
        evalOverview.setEvalAlgoIds(algoIds);
        evalOverview.setEvalAlgoNames(algoNames);
        evalOverviewMapper.updateById(evalOverview);
        sftLlmMapper.deleteById(sftLlm.getSftId());
        overviewModelRelationMapper.insert(OverviewModelRelation.builder()
                .modelId(llmTask.getLlmTaskId())
                .overviewId(sftLlm.getOverviewId())
                .modelName(sftLlm.getModelName())
                .modelType("LLM-SFT")
                .build());
        if(!StringUtils.isEmpty(sftLlm.getTestName())) {
            String runUrl = "http://192.168.3.39:8084/eval/detail/runTest/";
            DataInfo dataInfo = dataInfoMapper.selectById(sftLlm.getTestId());
            RunTestModelParam runTestModelParam = RunTestModelParam.builder()
                    .evalOverviewId(llmTask.getEvalOverviewId())
                    .modelId(llmTask.getLlmTaskId())
                    .modelName(llmTask.getLlmTaskName())
                    .evalDataId(sftLlm.getTestId())
                    .dataPath(dataInfo.getDataPath())
                    .evalDataName(dataInfo.getDataName())
                    .evalUserId(llmTask.getLlmCreateUserId())
                    .evalUserName(llmTask.getLlmCreateUserName())
                    .build();
            HttpUtil.sendPostDataByJson(runUrl, JSON.toJSONString(runTestModelParam));
        }
        return 1;
    }

    @Override
    public int updateSft(SftLlm sftLlm) {
        sftLlmMapper.updateById(sftLlm);
        return 1;
    }

    @Override
    public String getLogs(int sftId) {
        SftLlm sftLlm = sftLlmMapper.selectById(sftId);
        if(sftLlm == null) {
            return "已完成";
        }
        return sftLlm.getLogPath();
    }
}
