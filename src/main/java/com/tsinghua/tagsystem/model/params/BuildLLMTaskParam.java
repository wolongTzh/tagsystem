package com.tsinghua.tagsystem.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BuildLLMTaskParam {

    int evalUserId;

    String evalUserName;

    String evalOverviewName;

    String evalOverviewIntro;

    String token;

    String llmUpType;
}
