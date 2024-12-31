package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.ModelHelpTag;
import com.tsinghua.tagsystem.dao.mapper.ModelHelpTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-12-31
 */
@Service
public class ModelHelpTagManager extends ServiceImpl<ModelHelpTagMapper, ModelHelpTag> implements IService<ModelHelpTag> {
    @Autowired
    ModelHelpTagMapper modelHelpTagMapper;

}
