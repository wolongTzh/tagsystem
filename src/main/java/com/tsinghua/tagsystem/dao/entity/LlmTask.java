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
 * @since 2024-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class LlmTask implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "llm_task_id", type = IdType.AUTO)
    private Integer llmTaskId;

    private String llmTaskName;

    private LocalDateTime llmCreateTime;

    private Integer llmCreateUserId;

    private String llmCreateUserName;

    private String llmType;

    private String llmScriptPath;

    private String llmInputPath;

    private String llmOutputPath;

    private String formatDescribe;

    private Integer evalOverviewId;

    private String systemDescribe;


}
