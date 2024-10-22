package com.tsinghua.tagsystem.controller;

import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.ModelEval;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.service.ModelEvalService;
import com.tsinghua.tagsystem.service.impl.EvalOverviewServiceImpl;
import com.tsinghua.tagsystem.utils.HttpUtil;
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
        return WebUtil.successResult(evalOverviewService.getEvalOverview(userId));
    }

    @PostMapping(value = "addTask")
    @CrossOrigin
    public WebResInfo addTask(@RequestBody EvalOverview evalOverview) throws IOException {
        return WebUtil.successResult(evalOverviewService.addEvalOverview(evalOverview.getEvalOverviewName(), evalOverview.getEvalOverviewIntro(), evalOverview.getEvalOverviewUserId(), evalOverview.getEvalOverviewUserName()));
    }

    @PostMapping(value = "updateTask")
    @CrossOrigin
    public WebResInfo updateTask(@RequestBody EvalOverview evalOverview) throws IOException {
        evalOverviewService.updateEvalOverview(evalOverview.getEvalOverviewName(), evalOverview.getEvalOverviewIntro(), evalOverview.getEvalOverviewId());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "deleteTask")
    @CrossOrigin
    public WebResInfo deleteTask(@RequestBody EvalOverview evalOverview) throws IOException {
        evalOverviewService.deleteEvalOverview(evalOverview.getEvalOverviewId());
        return WebUtil.successResult("success");
    }
}
