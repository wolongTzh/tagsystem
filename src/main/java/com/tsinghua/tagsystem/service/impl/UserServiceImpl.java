package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserManager userManager;

    @Override
    public boolean signup(UserParam param) {
        return userManager.save(TsUser.builder()
                .name(param.getName())
                .password(param.getPassword())
                .username(param.getUsername())
                .identity("worker")
                .build());
    }

    @Override
    public TsUser login(UserParam param) {
        return userManager.getByUserMsg(param.getUsername(), param.getPassword());
    }

    @Override
    public TsUser getById(Integer userId) {
        return userManager.getByUserId(userId);
    }

    @Override
    public List<TsUser> getWorkers() {
        return userManager.getWorkers();
    }
}
