package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.RunBatchInfo;
import com.tsinghua.tagsystem.dao.mapper.RunBatchInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2025-04-09
 */
@Service
public class RunBatchInfoManager extends ServiceImpl<RunBatchInfoMapper, RunBatchInfo> implements IService<RunBatchInfo> {

    @Autowired
    RunBatchInfoMapper runBatchInfoMapper;
}
