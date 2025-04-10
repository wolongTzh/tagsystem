package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.entity.multi.RunBatchExtend;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.manager.LlmTaskManager;
import com.tsinghua.tagsystem.manager.ModelInfoManager;
import com.tsinghua.tagsystem.manager.OverviewModelRelationManager;
import com.tsinghua.tagsystem.manager.RunBatchInfoManager;
import com.tsinghua.tagsystem.service.WebsiteService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    @Autowired
    RunBatchInfoManager runBatchInfoManager;

    @Autowired
    OverviewModelRelationManager overviewModelRelationManager;

    @Autowired
    LlmTaskManager llmTaskManager;

    @Autowired
    ModelInfoManager modelInfoManager;

    String runBatchInterface;

    WebsiteServiceImpl(AlchemistPathConfig config) {
        runBatchInterface = config.getRunBatchInterface();
    }

    @Override
    public int insertRunBatchTask(RunBatchInfo runBatchInfo) {
        OverviewModelRelation overviewModelRelation = overviewModelRelationManager.getOne(new QueryWrapper<OverviewModelRelation>().eq("overview_id", runBatchInfo.getOverviewId()).eq("model_id", runBatchInfo.getModelId()));
        runBatchInfo.setModelType(overviewModelRelation.getModelType());
        runBatchInfo.setStatus("未开始");
        runBatchInfoManager.save(runBatchInfo);
        runBatchInfo.setDocPath("/home/tz/copy-code/web_generate/run_batch/" + runBatchInfo.getRbiId());
        runBatchInfoManager.updateById(runBatchInfo);
        return runBatchInfo.getRbiId();
    }

    @Override
    public int updateRunBatchById(RunBatchInfo runBatchInfo) {
        runBatchInfoManager.updateById(runBatchInfo);
        return 1;
    }

    @Override
    public int runBatchTask(RunBatchInfo runBatchInfo) throws IOException {
        String url = runBatchInterface;
        runBatchInfo.setStatus("正在运行");
        runBatchInfoManager.updateById(runBatchInfo);
        RunBatchExtend runBatchInfoExtend = (RunBatchExtend) runBatchInfo;
        if(runBatchInfo.getModelType().contains("LLM")) {
            runBatchInfoExtend.setLlmTask(llmTaskManager.getOne(new QueryWrapper<LlmTask>().eq("llm_task_id", runBatchInfo.getModelId())));
        }
        else if(runBatchInfo.getModelType().equals("SLM")) {
            runBatchInfoExtend.setModelInfo(modelInfoManager.getOne(new QueryWrapper<ModelInfo>().eq("model_id", runBatchInfo.getModelId())));
        }
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(runBatchInfoExtend));
        return 1;
    }

    @Override
    public int finishRunBatchTask(RunBatchInfo runBatchInfo) throws IOException {
        runBatchInfo.setStatus("已完成");
        runBatchInfoManager.updateById(runBatchInfo);
        return 1;
    }
}
