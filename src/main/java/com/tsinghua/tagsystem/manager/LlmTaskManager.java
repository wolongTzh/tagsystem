package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.dao.mapper.LlmTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-12-10
 */
@Service
public class LlmTaskManager extends ServiceImpl<LlmTaskMapper, LlmTask> implements IService<LlmTask> {

    @Autowired
    LlmTaskMapper llmTaskMapper;

}
