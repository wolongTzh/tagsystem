package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.AlgoInfo;
import com.tsinghua.tagsystem.dao.mapper.AlgoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-20
 */
@Service
public class AlgoInfoManager extends ServiceImpl<AlgoInfoMapper, AlgoInfo> implements IService<AlgoInfo> {
    @Autowired
    AlgoInfoMapper algoInfoMapper;

}
