package com.tsinghua.tagsystem.model;

import com.tsinghua.tagsystem.model.Relation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkerTaskMsg {

    Integer userId;

    String taskId;

    String title;

    Integer relationNum;

    Integer unTaggedNum;

    Integer start;

    Integer end;

    List<Relation> relationList;
}
