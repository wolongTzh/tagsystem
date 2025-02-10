package com.tsinghua.tagsystem.model.params;

import com.tsinghua.tagsystem.dao.entity.DataInfo;
import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.dao.entity.ModelHelpTag;
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
public class BuildAutoTestTaskParam {
    private static final long serialVersionUID=1L;

    Integer evalOverviewId;

    UploadTestDataParam uploadTestDataParam;

    ModelHelpTag modelHelpTag;

    String dataDefinitionInfo;
}
