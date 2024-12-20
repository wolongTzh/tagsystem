package com.tsinghua.tagsystem.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreateLLMTaskParam {

    Integer evalOverviewId;

    Integer evalUserId;

    String evalUserName;

    String llmTaskName;

    String llmType;

    String llmScriptPath;

    String llmInputPath;

    String formatDescribe;

    String systemDescribe;
}
