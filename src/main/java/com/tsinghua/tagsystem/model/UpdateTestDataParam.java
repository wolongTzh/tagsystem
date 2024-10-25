package com.tsinghua.tagsystem.model;

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
public class UpdateTestDataParam {
    private static final long serialVersionUID=1L;

    Integer evalOverviewId;

    Integer modelId;

    Integer evalDataId;

    Integer userId;

    String userName;

    String evalScore;

    String evalScoreDetail;
}
