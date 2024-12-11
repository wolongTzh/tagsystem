package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.dao.mapper.LlmTaskMapper;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.manager.UserManager;
import com.tsinghua.tagsystem.model.params.CreateLLMTaskParam;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.LLMTaskService;
import com.tsinghua.tagsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LLMTaskServiceImpl implements LLMTaskService {

    @Autowired
    LlmTaskMapper llmTaskMapper;

    @Override
    public int createLLMTask(CreateLLMTaskParam createLLMTaskParam) {
        LlmTask llmTask = LlmTask.builder()
                .llmCreateTime(LocalDateTime.now())
                .llmCreateUserId(createLLMTaskParam.getEvalUserId())
                .llmCreateUserName(createLLMTaskParam.getEvalUserName())
                .llmType(createLLMTaskParam.getLlmType())
                .formatDescribe(createLLMTaskParam.getFormatDescribe())
                .llmScriptPath(createLLMTaskParam.getLlmScriptPath())
                .evalOverviewId(createLLMTaskParam.getEvalOverviewId())
                .build();
        llmTaskMapper.insert(llmTask);
        return llmTask.getLlmTaskId();
    }
}
