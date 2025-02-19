package com.tsinghua.tagsystem.controller;

import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.WorkerTaskMsg;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.WorkerService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/worker")
@Slf4j
public class WorkerController {

    @Autowired
    WorkerService workerService;

    @GetMapping(value = "getTasks")
    public WebResInfo getTasks(Integer userId) throws IOException {
        WorkerTasksVO workerTasksVO = workerService.getTasks(userId);
        return WebUtil.successResult(workerTasksVO);
    }

    @GetMapping(value = "tagTask")
    public WebResInfo tagTask(String taskId) throws IOException {
        WorkerTaskMsg workerTaskMsg = workerService.tagTask(taskId);
        return WebUtil.successResult(workerTaskMsg);
    }

    @GetMapping(value = "checkTask")
    public WebResInfo checkTask(String taskId) throws IOException {
        CheckTaskVO checkTaskVO = workerService.checkTask(taskId);
        return WebUtil.successResult(checkTaskVO);
    }

    @GetMapping(value = "getRelationDefinition")
    public WebResInfo getRelationDefinition(String taskId) throws IOException {
        String relationDefinition = workerService.getRelationDefinition(taskId);
        return WebUtil.successResult(relationDefinition);
    }

    @PostMapping(value = "saveCheck")
    public WebResInfo saveCheck(@RequestBody SaveCheckParam param) throws IOException {
        boolean ret = workerService.saveCheck(param);
        return WebUtil.successResult(ret);
    }

    @PostMapping(value = "saveTag")
    public WebResInfo saveTag(@RequestBody WorkerTaskMsg workerTaskMsg) throws IOException {
        boolean ret = workerService.saveTag(workerTaskMsg);
        return WebUtil.successResult(ret);
    }
}
