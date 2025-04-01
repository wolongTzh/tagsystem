package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.SftLlm;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.FinishLLMTaskParam;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import com.tsinghua.tagsystem.queue.MessageQueue;
import com.tsinghua.tagsystem.service.SftLLMService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/eval/sft")
@Slf4j
public class SftController {

    @Autowired
    SftLLMService sftLLMService;

    @Autowired
    MessageQueue messageQueue;

    @PostMapping(value = "createSft")
    public WebResInfo createSft(SftLlm sftLlm) throws IOException {
        log.info("into llm createSft");
        log.info(JSON.toJSONString(sftLlm));
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(sftLLMService.createSft(sftLlm));
    }

    @PostMapping(value = "runSft")
    public WebResInfo runSft(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runSft");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("sft");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTestPromote(runTestModelParam);
        int evalDetailId = messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult(evalDetailId);
    }

    @PostMapping(value = "finishSft")
    public WebResInfo finishSft(@RequestBody FinishLLMTaskParam param) throws IOException {
        log.info("into llm finishSft");
        log.info(JSON.toJSONString(param));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
//        llmTaskService.finishLLMTask(param);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "updateSft")
    public WebResInfo updateSft(@RequestBody SftLlm sftLlm) throws IOException {
        log.info("into llm updateSft");
        log.info(JSON.toJSONString(sftLlm));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        sftLLMService.updateSft(sftLlm);
        return WebUtil.successResult("success");
    }
}
