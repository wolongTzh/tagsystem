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
 * @since 2023-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TrainData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "train_id", type = IdType.AUTO)
    private Integer trainId;

    private String trainName;

    private String trainCreator;

    private LocalDateTime trainGenTime;

    private String trainSource;

    private String trainRelaInfo;

    private String trainPath;


}
