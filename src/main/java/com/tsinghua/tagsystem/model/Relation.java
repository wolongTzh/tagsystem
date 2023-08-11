package com.tsinghua.tagsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Relation {

    Integer id;

    String text;

    int sourceStart;

    int sourceEnd;

    int targetStart;

    int targetEnd;

    String predicate;

    String status = "UNCHECKED";
}
