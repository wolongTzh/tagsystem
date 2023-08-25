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
public class EntityValue {
    private Integer start;

    private Integer end;

    private String text;

    private List<String> lables;
}
