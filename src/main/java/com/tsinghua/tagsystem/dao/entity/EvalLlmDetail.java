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
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class EvalLlmDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "eval_llm_detail_id", type = IdType.AUTO)
    private Integer evalLlmDetailId;

    private Integer evalOverviewId;

    private Integer llmTaskId;

    private Integer dataId;

    private LocalDateTime evalLlmDetailTime;

    private String evalLlmDetailScore;


}
