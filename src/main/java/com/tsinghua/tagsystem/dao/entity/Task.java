package com.tsinghua.tagsystem.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Task {

    private String taskId;

    private String title;

    private String status;

    private Integer relationNum;

    private Integer creatorId;

    private String filePath;

    private Integer uncheckedNum;

    private String checkWorker;

    private Integer evalOverviewId;

    private String relationDefinition;
}
