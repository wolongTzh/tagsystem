package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.mapper.EvalDetailMapper;
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
public class EvalDetailManager extends ServiceImpl<EvalDetailMapper, EvalDetail> implements IService<EvalDetail> {

    @Autowired
    EvalDetailMapper evalDetailMapper;
}
