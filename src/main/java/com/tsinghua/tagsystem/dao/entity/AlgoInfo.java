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
 * @since 2024-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AlgoInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "algo_id", type = IdType.AUTO)
    private Integer algoId;

    private String algoName;

    private String algoVersion;

    private String algoPath;

    private Integer algoCreatorId;

    private String algoCreatorName;

    private LocalDateTime algoGenTime;

    private String envPath;

    private String cmd;

    private String trainCmd;

    private Integer evalOverviewId;


}
