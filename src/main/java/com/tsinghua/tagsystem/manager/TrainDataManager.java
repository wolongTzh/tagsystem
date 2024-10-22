package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.TrainData;
import com.tsinghua.tagsystem.dao.mapper.TrainDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainDataManager extends ServiceImpl<TrainDataMapper, TrainData> implements IService<TrainData> {

    @Autowired
    TrainDataMapper trainDataMapper;
}
