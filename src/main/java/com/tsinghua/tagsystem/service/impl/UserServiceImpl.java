package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserManager userManager;

    @Override
    public boolean signup(UserParam param) {
        if(StringUtils.isEmpty(param.getUsername()) || StringUtils.isEmpty(param.getPassword()) || StringUtils.isEmpty(param.getName())) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if(userManager.getByUserName(param.getUsername()) != null) {
            throw new BusinessException(BusinessExceptionEnum.USER_ALREADY_EXIST);
        }
        return userManager.save(TsUser.builder()
                .name(param.getName())
                .password(param.getPassword())
                .username(param.getUsername())
                .identity("worker")
                .build());
    }

    @Override
    public TsUser login(UserParam param) {
        if(StringUtils.isEmpty(param.getUsername()) || StringUtils.isEmpty(param.getPassword())) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        TsUser tsUser = userManager.getByUserMsg(param.getUsername(), param.getPassword());
        if(tsUser == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_CHECK_ERROR);
        }
        return tsUser;
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
