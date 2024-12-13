package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.BuildCompareTaskParam;
import com.tsinghua.tagsystem.model.params.BuildLLMTaskParam;
import com.tsinghua.tagsystem.model.params.BuildPromoteTaskParam;
import com.tsinghua.tagsystem.model.params.CreateLLMTaskParam;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.impl.EvalOverviewServiceImpl;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/eval/llm")
@Slf4j
public class LLMController {

    @Autowired
    LLMTaskService llmTaskService;

    @PostMapping(value = "createTask")
    public WebResInfo createTask(CreateLLMTaskParam param) throws IOException {
        log.info("into llm createTask");
        log.info(JSON.toJSONString(param));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(llmTaskService.createLLMTask(param));
    }

    @GetMapping(value = "display")
    public WebResInfo display(Integer evalOverviewId) throws IOException {
        log.info("into llm display");
        log.info("evalOverviewId: " + evalOverviewId);
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(llmTaskService.display(evalOverviewId));
    }

    @PostMapping(value = "deleteModel")
    public WebResInfo deleteModel(int modelId, int evalOverviewId) throws IOException {
        log.info("into deleteModel");
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        llmTaskService.deleteModel(modelId, evalOverviewId);
        return WebUtil.successResult("success");
    }
}
