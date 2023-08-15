package com.tsinghua.tagsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CheckAtom {

    String status;

    Integer id;

    String text;

    int sourceStart;

    int sourceEnd;

    int targetStart;

    int targetEnd;

    String predicate;

    List<RelationSimple> relationList;
}
