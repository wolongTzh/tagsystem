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
 * @since 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EvalDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "eval_detail_id", type = IdType.AUTO)
    private Integer evalDetailId;

    private Integer evalOverviewId;

    private Integer modelId;

    private String modelName;

    private String evalScore;

    private String evalScoreDetail;

    private Integer evalDataId;

    private String evalDataName;

    private LocalDateTime evalTime;

    private Integer evalUserId;

    private String evalUserName;

    private Integer pid;


}
