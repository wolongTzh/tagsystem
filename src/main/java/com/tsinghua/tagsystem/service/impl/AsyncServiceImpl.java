package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.service.AsyncService;
import com.tsinghua.tagsystem.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    UserManager userManager;
    @Override
    public void asyncTrain() {
        System.out.println("开始执行异步方法");
        try {
            String cmd = "sh /home/tz/tagsystem/cmd.sh";
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        }
        catch (Exception e) {
            System.out.println("失败执行异步方法");
            e.printStackTrace();
        }
        System.out.println("结束执行异步方法");
        TsUser tsUser = userManager.getByUserName("tanzheng");
        tsUser.setIdentity("async");
        userManager.updateByUserId(tsUser);
        System.out.println("结束执行更细数据库");
    }
}
