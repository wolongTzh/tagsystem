package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.ModelTask;
import com.tsinghua.tagsystem.dao.mapper.ModelTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelTaskManager extends ServiceImpl<ModelTaskMapper, ModelTask> implements IService<ModelTask> {

    @Autowired
    ModelTaskMapper modelTaskMapper;
}
