package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.buildPromoteTaskParam;
import com.tsinghua.tagsystem.service.impl.EvalOverviewServiceImpl;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/eval/overview")
@Slf4j
public class EvalOverviewController {

    @Autowired
    EvalOverviewServiceImpl evalOverviewService;

    @GetMapping(value = "display")
    @CrossOrigin
    public WebResInfo display(int userId) throws IOException {
        log.info("into overview display");
//        EvalOverviewControllerUtil.validDisplayParam(userId);
        return WebUtil.successResult(evalOverviewService.getEvalOverview(userId));
    }

    @PostMapping(value = "addTask")
    @CrossOrigin
    public WebResInfo addTask(@RequestBody EvalOverview evalOverview) throws IOException {
        log.info("into overview addTask");
//        EvalOverviewControllerUtil.validAddTaskParam(evalOverview);
        return WebUtil.successResult(evalOverviewService.addEvalOverview(evalOverview));
    }

    @PostMapping(value = "updateTask")
    @CrossOrigin
    public WebResInfo updateTask(@RequestBody EvalOverview evalOverview) throws IOException {
        log.info("into overview updateTask");
//        EvalOverviewControllerUtil.validUpdateTaskParam(evalOverview);
        evalOverviewService.updateEvalOverview(evalOverview);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "deleteTask")
    @CrossOrigin
    public WebResInfo deleteTask(@RequestBody EvalOverview evalOverview) throws IOException {
        log.info("into overview deleteTask");
//        EvalOverviewControllerUtil.validDeleteTaskParam(evalOverview);
        evalOverviewService.deleteEvalOverview(evalOverview.getEvalOverviewId());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "createPromoteTask")
    public WebResInfo uploadTestData(buildPromoteTaskParam buildPromoteTaskParam) throws IOException {
        log.info("into createPromoteTask");
        log.info(JSON.toJSONString(buildPromoteTaskParam));
//        EvalDetailControllerUtil.validUpdateScoreParam(evalDetail);
        int algoId = evalOverviewService.buildPromoteTask(buildPromoteTaskParam);
        return WebUtil.successResult(algoId);
    }
}
