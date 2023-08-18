package com.tsinghua.tagsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkerTaskDTO {

    String taskId;

    String title;

    Integer relationNum;

    Integer untaggedNum;

    Integer taskType;

    Integer dataNum;

    String status;
}
