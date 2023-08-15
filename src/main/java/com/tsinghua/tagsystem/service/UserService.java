package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.model.params.UserParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public boolean signup(UserParam param);

    public TsUser login(UserParam param);

    public TsUser getById(Integer userId);

    public List<TsUser> getWorkers();
}
