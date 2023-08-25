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

    int source_id;

    String head;

    List<String> source_type;

    int targetStart;

    int targetEnd;

    int target_id;

    String tail;

    List<String> target_type;

    String predicate;

    List<RelationSimple> relationList;
}
