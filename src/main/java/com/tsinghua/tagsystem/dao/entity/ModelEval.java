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
 * @since 2024-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ModelEval implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "model_eval_id", type = IdType.AUTO)
    private Integer modelEvalId;

    private Integer modelId;

    private String modelName;

    private String modelScore;

    private String modelScoreDetail;

    private String modelEvalDataName;

    private LocalDateTime modelEvalTime;

    private Integer modelUserId;


}
