package com.tsinghua.tagsystem.controller;

import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
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
        return WebUtil.successResult(evalDetailService.getEvalDetail(evalOverviewId));
    }

    @PostMapping(value = "updateTable")
    public WebResInfo updateTable(@RequestBody EvalDetailDecorate evalDetailDecorate) throws IOException {
        evalDetailService.updateEvalDetail(evalDetailDecorate);
        return WebUtil.successResult("success");
    }
}
