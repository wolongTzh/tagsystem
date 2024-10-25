package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@NoArgsConstructor
public class ExistTestData extends DataInfo implements Serializable {

    private static final long serialVersionUID=1L;

    public ExistTestData(DataInfo dataInfo) {
        this.setDataId(dataInfo.getDataId());
        this.setDataName(dataInfo.getDataName());
        this.setDataPath(dataInfo.getDataPath());
    }
}
