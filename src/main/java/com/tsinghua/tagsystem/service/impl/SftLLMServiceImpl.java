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
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.SftLLMService;
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
public class SftLLMServiceImpl implements SftLLMService {

    @Autowired
    SftLlmMapper sftLlmMapper;

    @Autowired
    EvalDetailMapper evalDetailMapper;

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
    String vllmScriptInterface;

    SftLLMServiceImpl(AlchemistPathConfig config) {
        llmTaskInterface = config.getLlmTaskInterface();
        llmCalculateInterface = config.getLlmCalculateInterface();
        testDataPath = config.getTestDataPath();
        compareLLMInterface = config.getCompareLLMInterface();
        vllmScriptInterface = config.getVllmScriptInterface();
    }

    @Override
    public int createSft(SftLlm sftLlm) {
        sftLlm.setCreateTime(LocalDateTime.now());
        sftLlmMapper.insert(sftLlm);
        return sftLlm.getSftId();
    }
}
