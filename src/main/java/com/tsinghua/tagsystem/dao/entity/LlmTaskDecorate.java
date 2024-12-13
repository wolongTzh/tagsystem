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
import java.util.List;

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
public class LlmTaskDecorate implements Serializable {

    List<LlmTask> llmTaskList;
    List<EvalLlmDetail> evalDetailList;
    List<DataInfo> testDataList;
    String evalTestNames;
    String evalTestIds;
    String evalAlgoNames;
    String evalAlgoIds;

}
