package com.tsinghua.tagsystem.model.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ExportRelation {

    private Integer from_id;

    private Integer to_id;

    private String type;

    private List<String> lables;
}
