package com.tsinghua.tagsystem.model.VO;

import com.tsinghua.tagsystem.model.SubTaskMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GetTasksVO {

    String taskId;

    String title;

    Integer relationNum;

    String status;

    List<SubTaskMsg> subTaskMsgs;
}
