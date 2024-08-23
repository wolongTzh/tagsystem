package com.tsinghua.tagsystem.controller;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.AsyncService;
import com.tsinghua.tagsystem.service.ModelService;
import com.tsinghua.tagsystem.service.UserService;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "api/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ModelService modelService;

    @Autowired
    AsyncService asyncService;

    @PostMapping(value = "signup")
    public WebResInfo getTasks(@RequestBody UserParam param) throws IOException {
        boolean ret = userService.signup(param);
        return WebUtil.successResult(ret);
    }

    @PostMapping(value = "login")
    public WebResInfo tagTask(@RequestBody UserParam param) throws IOException {
        TsUser tsUser = userService.login(param);
        return WebUtil.successResult(tsUser);
    }

    @GetMapping(value = "getById")
    public WebResInfo saveTag(Integer userId) throws IOException {
        TsUser tsUser = userService.getById(userId);
        return WebUtil.successResult(tsUser);
    }

    @GetMapping(value = "getWorkers")
    public WebResInfo getWorkers() throws IOException {
        List<TsUser> workers = userService.getWorkers();
        return WebUtil.successResult(workers);
    }

    @GetMapping(value = "testModel")
    public WebResInfo testModel() throws IOException {
        try {
            String cmd = "python /home/tz/copy-code/W2NER-main/test_train.py";
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        }
        catch (Exception e) {
            System.out.println("失败执行异步方法");
            e.printStackTrace();
        }
        return WebUtil.successResult("sccess");
    }
}
