package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.OverviewUserRela;
import com.tsinghua.tagsystem.dao.mapper.OverviewUserRelaMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-27
 */
@Service
public class OverviewUserRelaManager extends ServiceImpl<OverviewUserRelaMapper, OverviewUserRela> implements IService<OverviewUserRela> {

}
