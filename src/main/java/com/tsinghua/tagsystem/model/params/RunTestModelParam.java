package com.tsinghua.tagsystem.model.params;

import com.tsinghua.tagsystem.dao.entity.LlmTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class RunTestModelParam {
    private static final long serialVersionUID=1L;

    Integer evalDetailId;

    Integer evalOverviewId;

    Integer modelHelpTagId;

    String modelHelpType;

    Integer modelId;

    String modelName;

    Integer evalDataId;

    String evalDataName;

    String dataPath;

    Integer evalUserId;

    String evalUserName;

    String modelPath;

    String modelFilePath;

    String codeName;

    String envPath;

    String cmd;

    String taskType;

    String modelType;

    String modelSuffix;

    String checkPointPath;

    String token;

    LlmTask llmTask;
}
