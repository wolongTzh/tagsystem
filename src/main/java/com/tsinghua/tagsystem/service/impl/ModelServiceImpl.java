package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    UserManager userManager;

    @Override
    public void train() {
        System.out.println("结束执行异步方法");
        TsUser tsUser = userManager.getByUserName("tanzheng");
        tsUser.setIdentity("async");
        userManager.updateByUserId(tsUser);
        System.out.println("结束执行更细数据库");
    }
}
