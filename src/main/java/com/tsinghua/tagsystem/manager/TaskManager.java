package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.multi.ManagerTask;
import com.tsinghua.tagsystem.dao.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskManager extends ServiceImpl<TaskMapper, Task> implements IService<Task> {

    @Autowired
    TaskMapper taskMapper;

    public List<ManagerTask> getManagerTaskList(Integer userId) {
        return taskMapper.getManagerTaskList(userId);
    }

    public List<ManagerTask> getManagerCheckTaskList(String taskId) {
        return taskMapper.getManagerCheckTaskList(taskId);
    }

    public Task getByTaskId(String taskId) {
        return this.getOne(new QueryWrapper<Task>()
                .lambda()
                .eq(Task::getTaskId, taskId));
    }

    public void updateByTaskId(Task task) {
        this.update(task, new QueryWrapper<Task>()
                .lambda()
                .eq(Task::getTaskId, task.getTaskId()));
    }
}
