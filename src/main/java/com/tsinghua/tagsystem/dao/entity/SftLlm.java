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
 * @since 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SftLlm implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "sft_id", type = IdType.AUTO)
    private Integer sftId;

    private String modelName;

    private String modelType;

    private LocalDateTime createTime;

    private String status;

    private String sftDataPath;

    private Integer userId;

    private Integer overviewId;

    private String systemPrompt;

    private Integer testId;

    private String testName;

    private String userName;
}
