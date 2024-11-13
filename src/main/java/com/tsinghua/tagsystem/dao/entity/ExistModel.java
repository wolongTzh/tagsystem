package com.tsinghua.tagsystem.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

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
public class ExistModel extends ModelInfo implements Serializable {

    private static final long serialVersionUID=1L;

    public ExistModel(ModelInfo modelInfo) {
        this.setModelId(modelInfo.getModelId());
        this.setModelName(modelInfo.getModelName());
    }
}
