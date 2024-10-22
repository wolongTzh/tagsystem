package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ModelTask implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "mt_id", type = IdType.AUTO)
    private Integer mtId;

    private String mtName;

    private String mtType;

    private String mtCreator;

    private LocalDateTime mtTime;

    private String mtStatus;

    private String mtTrainData;

    private String mtAlgorithm;

    private String mtDoc;

    private String modelName;


}
