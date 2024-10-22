package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.PendingData;
import com.tsinghua.tagsystem.dao.mapper.PendingDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingDataManager extends ServiceImpl<PendingDataMapper, PendingData> implements IService<PendingData> {

    @Autowired
    PendingDataMapper pendingDataMapper;
}
