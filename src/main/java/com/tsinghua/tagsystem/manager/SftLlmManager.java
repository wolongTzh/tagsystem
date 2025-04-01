package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.SftLlm;
import com.tsinghua.tagsystem.dao.mapper.SftLlmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2025-04-01
 */
@Service
public class SftLlmManager extends ServiceImpl<SftLlmMapper, SftLlm> implements IService<SftLlm> {

    @Autowired
    SftLlmMapper sftLlmMapper;
}
