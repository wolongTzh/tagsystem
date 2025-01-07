package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ModelHelpTag implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "model_help_tag_id", type = IdType.AUTO)
    private Integer modelHelpTagId;

    private Integer modelId;

    private String modelName;

    private String modelPath;

    private String dataPath;

    private String outputPath;

    private String imageName;

    private Integer evalOverviewId;



}
