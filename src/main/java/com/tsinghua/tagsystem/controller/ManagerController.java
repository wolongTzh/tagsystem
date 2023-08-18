package com.tsinghua.tagsystem.controller;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.model.ManagerTaskSupervise;
import com.tsinghua.tagsystem.model.VO.GetTasksVO;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/manager")
@Slf4j
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @PostMapping(value = "createTask")
    public WebResInfo createTask(@RequestParam("file") MultipartFile rawFile, @RequestParam("param") String rawParam) throws IOException {
        CreateTaskParam param = JSON.parseObject(rawParam, CreateTaskParam.class);
        File file = File.createTempFile("temp", "json");
        rawFile.transferTo(file);
        param.setFile(file);
        managerService.createTask(param);
        return WebUtil.successResult(null);
    }

    @GetMapping(value = "getTasks")
    public WebResInfo getTasks(Integer userId) throws IOException {
        GetTasksVO getTasksVO = managerService.getTasks(userId);
        return WebUtil.successResult(getTasksVO);
    }

    @GetMapping(value = "checkTask")
    public WebResInfo checkTask(String taskId) throws IOException {
        ManagerTaskSupervise managerTaskSupervise = managerService.checkTask(taskId);
        return WebUtil.successResult(managerTaskSupervise);
    }

    @PostMapping(value = "saveCheck")
    public WebResInfo saveCheck(@RequestBody ManagerTaskSupervise managerTaskSupervise) throws IOException {
        boolean ret = managerService.saveCheck(managerTaskSupervise);
        return WebUtil.successResult(ret);
    }
}
