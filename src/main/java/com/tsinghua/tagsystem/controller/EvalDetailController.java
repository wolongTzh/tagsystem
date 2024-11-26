package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.model.params.uploadCheckpointParam;
import com.tsinghua.tagsystem.model.params.UploadModelParam;
import com.tsinghua.tagsystem.model.params.UploadTestDataParam;
import com.tsinghua.tagsystem.model.params.runTestModelParam;
import com.tsinghua.tagsystem.model.WebResInfo;
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

    @GetMapping(value = "display")
    public WebResInfo display(int evalOverviewId) throws IOException {
        log.info("into detail display");
//        EvalDetailControllerUtil.validDisplayParam(evalOverviewId);
        return WebUtil.successResult(evalDetailService.getEvalDetail(evalOverviewId));
    }

    @PostMapping(value = "updateTable")
    public WebResInfo updateTable(@RequestBody EvalDetailDecorate evalDetailDecorate) throws IOException {
        log.info("into updateTable");
//        EvalDetailControllerUtil.validUpdateTableParam(evalDetailDecorate);
        evalDetailService.updateEvalDetail(evalDetailDecorate);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTest")
    public WebResInfo runTest(@RequestBody runTestModelParam runTestModelParam) throws IOException {
        log.info("into runTest");
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
        evalDetailService.runTest(runTestModelParam);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTestPromote")
    public WebResInfo runTestModel(@RequestBody runTestModelParam runTestModelParam) throws IOException {
        log.info("into runTestPromote");
        log.info(JSON.toJSONString(runTestModelParam));
//        EvalDetailControllerUtil.validTestModelParam(testModelParam);
        evalDetailService.runTestPromote(runTestModelParam);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "deleteTestResult")
    public WebResInfo deleteTestResult(@RequestBody EvalDetail evalDetail) throws IOException {
        log.info("into deleteTestResult");
//        EvalDetailControllerUtil.validDeleteTestResultParam(evalDetail);
        evalDetailService.delTestResult(evalDetail.getEvalDetailId());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "updateScore")
    public WebResInfo updateScore(@RequestBody EvalDetail evalDetail) throws IOException {
        log.info("into updateScore");
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        evalDetailService.addNewScore(evalDetail);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "uploadTestData")
    public WebResInfo uploadTestData(UploadTestDataParam uploadTestDataParam) throws IOException {
        log.info("into uploadTestData");
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int testDataId = evalDetailService.uploadTestData(uploadTestDataParam);
        return WebUtil.successResult(testDataId);
    }

    @PostMapping(value = "uploadModelData")
    public WebResInfo uploadTestData(UploadModelParam uploadModelParam) throws IOException {
        log.info("into uploadModelData");
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.uploadModel(uploadModelParam);
        return WebUtil.successResult(modelId);
    }

    @PostMapping(value = "uploadCheckpoint")
    public WebResInfo uploadCheckpoint(uploadCheckpointParam uploadCheckpointParam) throws IOException {
        log.info("into uploadCheckpoint");
        log.info(JSON.toJSONString(uploadCheckpointParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int modelId = evalDetailService.uploadCheckpoint(uploadCheckpointParam);
        return WebUtil.successResult(modelId);
    }
}
