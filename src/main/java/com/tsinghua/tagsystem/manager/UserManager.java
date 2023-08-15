package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.dao.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserManager extends ServiceImpl<UserMapper, TsUser> implements IService<TsUser> {

    public TsUser getByUserId(Integer userId) {
        return this.getOne(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getUserId, userId));
    }

    public TsUser getByUserMsg(String username, String password) {
        return this.getOne(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getUsername, username)
                .eq(TsUser::getPassword, password));
    }

    public List<TsUser> getWorkers() {
        return this.list(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getIdentity, "worker"));
    }
}
