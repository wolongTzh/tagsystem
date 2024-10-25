package com.tsinghua.tagsystem.model;

import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.ExistModel;
import com.tsinghua.tagsystem.dao.entity.ExistTestData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
public class TestModelParam {
    private static final long serialVersionUID=1L;

    Integer evalOverviewId;

    Integer modelId;

    Integer evalDataId;

    Integer evalUserId;

    String evalUserName;
}
