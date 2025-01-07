package com.tsinghua.tagsystem.model;

import com.tsinghua.tagsystem.dao.entity.ModelHelpTag;
import lombok.Data;

@Data
public class ModelHelpTagDTO extends ModelHelpTag {
    private String fee;
}
