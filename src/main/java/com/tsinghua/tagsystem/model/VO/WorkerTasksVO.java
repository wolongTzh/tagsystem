package com.tsinghua.tagsystem.model.VO;

import com.tsinghua.tagsystem.model.WorkerTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkerTasksVO {

    List<WorkerTaskDTO> workerTasks;
}
