package com.tsinghua.tagsystem.dao.entity.multi;

import com.tsinghua.tagsystem.dao.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ManagerTask extends Task {

    String subTaskId;

    String subTaskStatus;

    String subFilePath;
}
