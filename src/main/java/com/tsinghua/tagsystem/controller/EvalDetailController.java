package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.queue.MessageQueue;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/eval/detail")
@Slf4j
public class EvalDetailController {

    @Autowired
    EvalDetailService evalDetailService;

    @Autowired
    MessageQueue messageQueue;

    @GetMapping(value = "display")
    public WebResInfo display(int evalOverviewId) throws IOException {
        log.info("into detail display");
        log.info("evalOverviewId: " + evalOverviewId);
//        EvalDetailControllerUtil.validDisplayParam(evalOverviewId);
        return WebUtil.successResult(evalDetailService.getEvalDetail(evalOverviewId));
    }

    @PostMapping(value = "updateTable")
    public WebResInfo updateTable(@RequestBody EvalDetailDecorate evalDetailDecorate) throws IOException {
        log.info("into updateTable");
        log.info(JSON.toJSONString(evalDetailDecorate));
//        EvalDetailControllerUtil.validUpdateTableParam(evalDetailDecorate);
        evalDetailService.updateEvalDetail(evalDetailDecorate);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTest")
    public WebResInfo runTest(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runTest");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("customize");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTest(runTestModelParam);
        messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTestPromote")
    public WebResInfo runTestModel(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runTestPromote");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("promote");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTestPromote(runTestModelParam);
        messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTestHug")
    public WebResInfo runTestCompare(@RequestBody RunTestModelParam runTestModelParam) throws IOException {
        log.info("into runTestHug");
        log.info(JSON.toJSONString(runTestModelParam));
        runTestModelParam.setTaskType("compare");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
//        evalDetailService.runTestHug(runTestModelParam);
        messageQueue.enqueue(runTestModelParam);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "deleteTestResult")
    public WebResInfo deleteTestResult(@RequestBody EvalDetail evalDetail) throws IOException {
        log.info("into deleteTestResult");
        log.info(JSON.toJSONString(evalDetail));
//        EvalDetailControllerUtil.validDeleteTestResultParam(evalDetail);
        evalDetailService.delTestResult(evalDetail.getEvalDetailId());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "updateScore")
    public WebResInfo updateScore(@RequestBody EvalDetail evalDetail) throws IOException {
        log.info("into updateScore");
        log.info(JSON.toJSONString(evalDetail));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        evalDetailService.updateScore(evalDetail);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "addScore")
    public WebResInfo addScore(@RequestBody EvalDetail evalDetail) throws IOException {
        log.info("into addScore");
        log.info(JSON.toJSONString(evalDetail));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        Integer evalDetailId = evalDetailService.addNewScore(evalDetail);
        return WebUtil.successResult(evalDetailId);
    }

    @PostMapping(value = "uploadTestData")
    public WebResInfo uploadTestData(UploadTestDataParam uploadTestDataParam) throws IOException {
        log.info("into uploadTestData");
//        log.info(JSON.toJSONString(uploadTestDataParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int testDataId = evalDetailService.uploadTestData(uploadTestDataParam);
        return WebUtil.successResult(testDataId);
    }

    @PostMapping(value = "uploadModelData")
    public WebResInfo uploadTestData(UploadModelParam uploadModelParam) throws IOException {
        log.info("into uploadModelData");
//        log.info(JSON.toJSONString(uploadModelParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.uploadModel(uploadModelParam);
        return WebUtil.successResult(modelId);
    }

    @PostMapping(value = "uploadCheckpoint")
    public WebResInfo uploadCheckpoint(UploadCheckpointParam uploadCheckpointParam) throws IOException {
        log.info("into uploadCheckpoint");
//        log.info(JSON.toJSONString(uploadCheckpointParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.uploadCheckpoint(uploadCheckpointParam);
        return WebUtil.successResult(modelId);
    }

    @PostMapping(value = "uploadHugModel")
    public WebResInfo uploadHugModel(@RequestBody UploadHugModelParam uploadHugModelParam) throws IOException {
        log.info("into uploadHugModel");
        log.info(JSON.toJSONString(uploadHugModelParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.uploadHugModel(uploadHugModelParam);
        return WebUtil.successResult(modelId);
    }

    @PostMapping(value = "stopTask")
    public WebResInfo stopTask(@RequestBody StopTaskParam param) throws IOException {
        log.info("into stopTask");
        log.info(JSON.toJSONString(param));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.stopTask(param);
        return WebUtil.successResult(modelId);
    }
}
