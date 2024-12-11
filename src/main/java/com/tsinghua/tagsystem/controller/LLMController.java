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

    @GetMapping(value = "createTask")
    @CrossOrigin
    public WebResInfo createTask(CreateLLMTaskParam param) throws IOException {
        log.info("into llm createTask");
        log.info(JSON.toJSONString(param));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(llmTaskService.createLLMTask(param));
    }
}
