package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
 * @since 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ModelInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "model_id", type = IdType.AUTO)
    private Integer modelId;

    private String modelName;

    private String modelPath;

    private String modelCreator;

    private Integer modelCreatorId;

    private LocalDateTime modelGenTime;

    private String modelTrainDataName;

    private Double modelRecall;

    private Double modelPre;

    private Double modelF1;

    private String modelRecallDetail;

    private String modelPreDetail;

    private String modelF1Detail;

    private String modelVersion;

    private String envPath;

    private String cmd;

    private int algoId;

}
