package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.ModelInfo;
import com.tsinghua.tagsystem.dao.mapper.ModelInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelInfoManager extends ServiceImpl<ModelInfoMapper, ModelInfo> implements IService<ModelInfo> {

    @Autowired
    ModelInfoMapper modelInfoMapper;
}
