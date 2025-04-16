package com.tsinghua.tagsystem.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WebsiteInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "web_id", type = IdType.AUTO)
    private Integer webId;

    private String indexPath;

    private Integer overviewId;

    private String codePath;

    private String template;

    private String websiteName;


}
