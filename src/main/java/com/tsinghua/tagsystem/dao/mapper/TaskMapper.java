package com.tsinghua.tagsystem.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.model.ManagerTaskDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaskMapper extends BaseMapper<Task> {
    @Select("select task.*, sub_task.sub_task_id, sub_task.status subTaskStatus, sub_task.tag_workers taggingWorker from task,sub_task where task.creator_id = #{userId} and task.task_id = sub_task.parent_id order by task.task_id")
    List<ManagerTask> getManagerTaskList(Integer userId);

    @Select("select task.*, sub_task.file_path sub_file_path, sub_task.status subTaskStatus from task,sub_task,worker_task_rela where worker_task_rela.rela_id = #{taskId} and task.task_id = worker_task_rela.task_id and task.task_id = sub_task.parent_id")
    List<ManagerTask> getManagerCheckTaskList(String taskId);
}
