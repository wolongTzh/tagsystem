package com.tsinghua.tagsystem.model.params;

import com.tsinghua.tagsystem.dao.entity.LlmTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class StartLLMTaskParam {
    LlmTask llmTask;

    Integer llmTaskId;

    Integer evalOverviewId;

    String token;
}
