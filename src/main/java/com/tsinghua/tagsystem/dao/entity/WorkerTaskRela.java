package com.tsinghua.tagsystem.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkerTaskRela {

    private String relaId;

    private String taskId;

    private Integer userId;

    private Integer start;

    private Integer end;

    private String status;

    private Integer untaggedNum;
}
