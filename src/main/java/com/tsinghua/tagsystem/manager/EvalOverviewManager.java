package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-10-15
 */
@Service
public class EvalOverviewManager extends ServiceImpl<EvalOverviewMapper, EvalOverview> implements IService<EvalOverview> {

    @Autowired
    EvalOverviewMapper evalOverviewMapper;
}
