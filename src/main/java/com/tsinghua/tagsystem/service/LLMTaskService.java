package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.LlmTaskDecorate;
import com.tsinghua.tagsystem.model.params.CreateLLMTaskParam;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface LLMTaskService {

   public int createLLMTask(CreateLLMTaskParam createLLMTaskParam);

   public LlmTaskDecorate display(int evalOverviewId);

   public int deleteModel(int modelId, int evalOverviewId);
}
