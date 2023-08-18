package com.tsinghua.tagsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ManagerTaskDTO {

    String taskId;

    String title;

    Integer relationNum;

    String status;

    String checkingWorker;

    List<SubTaskMsg> subTaskMsgs;
}
