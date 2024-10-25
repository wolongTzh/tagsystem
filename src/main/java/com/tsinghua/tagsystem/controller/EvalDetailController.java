package com.tsinghua.tagsystem.controller;

import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.model.TestModelParam;
import com.tsinghua.tagsystem.model.UpdateTestDataParam;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.utils.HttpUtil;
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
        return WebUtil.successResult(evalDetailService.getEvalDetail(evalOverviewId));
    }

    @PostMapping(value = "updateTable")
    public WebResInfo updateTable(@RequestBody EvalDetailDecorate evalDetailDecorate) throws IOException {
        evalDetailService.updateEvalDetail(evalDetailDecorate);
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "runTest")
    public WebResInfo testModel(@RequestBody TestModelParam testModelParam) throws IOException {
        String url = "http://192.168.3.39:8081/algo_start";
        HttpUtil.sendPostDataByJson(url, testModelParam.toString());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "deleteTestResult")
    public WebResInfo deleteTestResult(@RequestBody EvalDetail evalDetail) throws IOException {
        evalDetailService.delTestResult(evalDetail.getEvalDetailId());
        return WebUtil.successResult("success");
    }

    @PostMapping(value = "updateScore")
    public WebResInfo updateScore(@RequestBody EvalDetail evalDetail) throws IOException {
        evalDetailService.AddNewScore(evalDetail);
        return WebUtil.successResult("success");
    }
}
