package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.EvalLlmDetail;
import com.tsinghua.tagsystem.dao.mapper.EvalLlmDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-12-13
 */
@Service
public class EvalLlmDetailManager extends ServiceImpl<EvalLlmDetailMapper, EvalLlmDetail> implements IService<EvalLlmDetail> {

    @Autowired
    EvalLlmDetailMapper evalLlmDetailMapper;

}
