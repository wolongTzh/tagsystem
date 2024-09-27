package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tsinghua.tagsystem.dao.entity.ModelEval;
import com.tsinghua.tagsystem.model.ManagerTaskSupervise;
import com.tsinghua.tagsystem.model.VO.ManagerTasksVO;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.service.ModelEvalService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/modeleval")
@Slf4j
public class ModelEvalController {

    @Autowired
    ModelEvalService modelEvalService;

    @PostMapping(value = "createModelEval")
    public WebResInfo createModelEval(@RequestBody ModelEval modelEval) throws IOException {
        modelEvalService.createModelEval(modelEval);
        System.out.println(modelEval);
        return WebUtil.successResult("success");
    }

    @GetMapping(value = "testModel")
    public WebResInfo testModel(String modelId, String dataId) throws IOException {
        String url = "http://192.168.3.39:8081/eval_model/?modelId=" + modelId + "&dataId=" + dataId;
        HttpUtil.sendGetData(url);
        return WebUtil.successResult("success");
    }
}
