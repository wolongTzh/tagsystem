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
public class PendingData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "pending_id", type = IdType.AUTO)
    private Integer pendingId;

    private String pendingName;

    private String pendingCreator;

    private LocalDateTime pendingTime;

    private String pendingModel;

    private String pendingDoc;

    private String pendingPath;


}
