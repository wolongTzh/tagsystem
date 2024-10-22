package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.DataInfo;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-10-22
 */
@Service
public class DataInfoManager extends ServiceImpl<DataInfoMapper, DataInfo> implements IService<DataInfo> {

    @Autowired
    DataInfoMapper dataInfoMapper;
}
