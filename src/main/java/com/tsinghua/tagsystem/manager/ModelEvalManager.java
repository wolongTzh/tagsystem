package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.ModelEval;
import com.tsinghua.tagsystem.dao.mapper.ModelEvalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-09-27
 */
@Service
public class ModelEvalManager extends ServiceImpl<ModelEvalMapper, ModelEval> implements IService<ModelEval> {

    @Autowired
    ModelEvalMapper modelEvalMapper;
}
