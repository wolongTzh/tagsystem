package com.tsinghua.tagsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class LLMTaskScoreCalHelper {

    Integer llmTaskId;

    Integer testDataId;

    String llmTaskName;

    String testDataPath;

    String llmOutPutPath;

}
