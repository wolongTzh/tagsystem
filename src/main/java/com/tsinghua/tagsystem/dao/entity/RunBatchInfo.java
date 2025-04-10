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
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RunBatchInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "rbi_id", type = IdType.AUTO)
    private Integer rbiId;

    private Integer modelId;

    private String modelType;

    private Integer overviewId;

    private String docPath;

    private String jsonOutputPath;

    private String status;

    private String imageOutputPath;

    private String dbName;

    private String scriptPath;

    private String taskName;

}
