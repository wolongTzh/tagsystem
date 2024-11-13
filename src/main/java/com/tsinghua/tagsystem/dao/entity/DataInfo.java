package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
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
 * @since 2024-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DataInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "data_id", type = IdType.AUTO)
    private Integer dataId;

    private String dataName;

    private Integer dataCreatorId;

    private String dataCreatorName;

    private LocalDateTime dataGenTime;

    private String dataType;

    private String dataRelaInfo;

    private String dataPath;


}
