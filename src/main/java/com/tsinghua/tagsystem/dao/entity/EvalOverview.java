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
 * @since 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class EvalOverview implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "eval_overview_id", type = IdType.AUTO)
    public Integer evalOverviewId;

    public String evalOverviewName;

    public String evalOverviewIntro;

    public String evalTestIds;

    public String evalTestNames;

    public String evalAlgoIds;

    public String evalAlgoNames;

    public String evalTrainingModelId;

    public Integer evalAlreadyRunNum = 0;

    public LocalDateTime evalOverviewTime;

    public Integer evalOverviewUserId;

    public String evalOverviewUserName;

    public String evalOverviewType;

    public String llmUpType;

    public String token;

    public String fee;


}
