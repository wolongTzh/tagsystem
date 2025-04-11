package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.RunBatchInfo;
import com.tsinghua.tagsystem.dao.entity.WebsiteInfo;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import com.tsinghua.tagsystem.queue.MessageQueue;
import com.tsinghua.tagsystem.service.WebsiteService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/eval/website")
@Slf4j
public class WebsiteController {

    @Autowired
    WebsiteService websiteService;

    @Autowired
    MessageQueue messageQueue;

    @PostMapping(value = "createRunBatchTask")
    public WebResInfo createTask(RunBatchInfo runBatchInfo) throws IOException {
        log.info("into llm createRunBatchTask");
        log.info(JSON.toJSONString(runBatchInfo));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(websiteService.insertRunBatchTask(runBatchInfo));
    }

    @PostMapping(value = "runTestRunBatch")
    public WebResInfo runTestModel(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runTestRunBatch");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("runBatch");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTestPromote(runTestModelParam);
        int evalDetailId = messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult(evalDetailId);
    }

    @PostMapping(value = "createWebsiteTask")
    public WebResInfo createWebsiteTask(WebsiteInfo websiteInfo) throws IOException {
        log.info("into llm createWebsiteTask");
        log.info(JSON.toJSONString(websiteInfo));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(websiteService.insertWebsiteTask(websiteInfo));
    }

    @GetMapping(value = "getAllDB")
    public WebResInfo getAllDB(int overviewId) throws IOException {
        log.info("into llm getAllDB");
        log.info(JSON.toJSONString(overviewId));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(websiteService.getAllDB(overviewId));
    }
}
