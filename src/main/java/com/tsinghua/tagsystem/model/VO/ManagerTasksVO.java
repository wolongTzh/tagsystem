package com.tsinghua.tagsystem.model.VO;

import com.tsinghua.tagsystem.model.ManagerTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ManagerTasksVO {

    List<ManagerTaskDTO> managerTaskList;
}
