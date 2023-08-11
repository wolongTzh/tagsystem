package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import com.tsinghua.tagsystem.dao.mapper.SubTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskManager extends ServiceImpl<SubTaskMapper, SubTask> implements IService<SubTask> {

    @Autowired
    SubTaskMapper subTaskMapper;

    public SubTask getByTaskId(String taskId) {
        return this.getOne(new QueryWrapper<SubTask>()
                .lambda()
                .eq(SubTask::getSubTaskId, taskId));
    }

    public boolean updateByTaskId(SubTask subTask) {
        return this.update(subTask, new QueryWrapper<SubTask>()
                .lambda()
                .eq(SubTask::getSubTaskId, subTask.getSubTaskId()));
    }

    public List<SubTask> judgeTaskFinish(String taskId) {
        return subTaskMapper.judgeTaskFinish(taskId);
    }
}
