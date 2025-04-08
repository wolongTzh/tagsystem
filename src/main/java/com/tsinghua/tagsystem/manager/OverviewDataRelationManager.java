package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.OverviewDataRelation;
import com.tsinghua.tagsystem.dao.mapper.OverviewDataRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2025-04-08
 */
@Service
public class OverviewDataRelationManager extends ServiceImpl<OverviewDataRelationMapper, OverviewDataRelation> implements IService<OverviewDataRelation> {

    @Autowired
    OverviewDataRelationMapper overviewDataRelationMapper;
}
