package com.tsinghua.tagsystem.dao.entity.multi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkerTask {

    String taskId;

    String subTaskId;

    String title;

    Integer relationNum;

    Integer untaggedNum;

    String taskType;

    Integer start;

    Integer end;

    String status;
}
