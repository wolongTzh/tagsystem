package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.model.LLMTaskScoreCalHelper;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.queue.MessageQueue;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.impl.EvalOverviewServiceImpl;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/eval/llm")
@Slf4j
public class LLMController {

    @Autowired
    LLMTaskService llmTaskService;

    @Autowired
    MessageQueue messageQueue;

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

    @PostMapping(value = "deleteTestData")
    public WebResInfo deleteTestData(int testId, int evalOverviewId) throws IOException {
        log.info("into deleteTestData");
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        llmTaskService.deleteTestData(testId, evalOverviewId);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTestLLM")
    public WebResInfo runTestModel(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runTestLLM");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("LLM");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTestPromote(runTestModelParam);
        int evalDetailId = messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult(evalDetailId);
    }

    @PostMapping(value = "vllmTaskScript")
    public WebResInfo vllmTaskScript(String modelResultPath, int testDataId, String scriptPath, int detailId) throws IOException {
        log.info("into vllmTaskScript");
        log.info(modelResultPath + " " + testDataId + " " + scriptPath + " " + detailId);
        int evalDetailId = llmTaskService.vllmTaskScript(modelResultPath, testDataId, scriptPath, detailId);
        return WebUtil.successResult(evalDetailId);
    }

    @PostMapping(value = "updateStatus")
    public WebResInfo updateStatus(@RequestBody LlmTask llmTask) throws IOException {
        log.info("into llm updateStatus");
        log.info(JSON.toJSONString(llmTask));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        llmTaskService.updateStatus(llmTask);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "finishLLMTask")
    public WebResInfo finishLLMTask(@RequestBody FinishLLMTaskParam param) throws IOException {
        log.info("into llm finishLLMTask");
        log.info(JSON.toJSONString(param));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        llmTaskService.finishLLMTask(param);
        return WebUtil.successResult("success");
    }
    @PostMapping(value = "comparePredictAnswer")
    public WebResInfo comparePredictAnswer(int modelId, int testDataId) throws IOException {
        log.info("into llm comparePredictAnswer");
        log.info(modelId + " " + testDataId);
        String modelType = llmTaskService.comparePredictAnswer(modelId, testDataId);
        return WebUtil.successResult(modelType);
    }


    @PostMapping(value = "addLLMDetailRelation")
    public WebResInfo addLLMDetailRelation(@RequestBody List<AddLLMDetailRelationParam> param) throws IOException {
        log.info("into llm addLLMDetailRelation");
        log.info(JSON.toJSONString(param));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        llmTaskService.addLLMDetailRelation(param);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "uploadTestData")
    public WebResInfo uploadTestData(UploadTestDataParam uploadTestDataParam) throws IOException {
        log.info("into llm uploadTestData");
//        log.info(JSON.toJSONString(uploadTestDataParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int testDataId = llmTaskService.uploadTestData(uploadTestDataParam);
        return WebUtil.successResult(testDataId);
    }

    @PostMapping(value = "genLog")
    public WebResInfo genLog(int evalOverviewId) throws IOException {
        log.info("into genLog");
//        log.info(JSON.toJSONString(buildCompareTaskParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        String log = llmTaskService.genLog(evalOverviewId);
        return WebUtil.successResult(log);
    }
}
