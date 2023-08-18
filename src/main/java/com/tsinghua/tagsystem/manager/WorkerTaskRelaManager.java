package com.tsinghua.tagsystem.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsinghua.tagsystem.dao.entity.Task;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.WorkerTask;
import com.tsinghua.tagsystem.dao.mapper.WorkerTaskRelaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkerTaskRelaManager extends ServiceImpl<WorkerTaskRelaMapper, WorkerTaskRela> implements IService<WorkerTaskRela> {
    @Autowired
    WorkerTaskRelaMapper workerTaskRelaMapper;

    public List<WorkerTask> getTasksTag(Integer userId) {
        return workerTaskRelaMapper.getManagerTaskListTag(userId);
    }

    public List<WorkerTask> getTasksCheck(Integer userId) {
        return workerTaskRelaMapper.getManagerTaskListCheck(userId);
    }

    public WorkerTask getTask(String taskId) {
        return workerTaskRelaMapper.getManagerTask(taskId);
    }

    public List<WorkerTaskRela> judgeGroupFinish(String taskId) {
        return workerTaskRelaMapper.judgeGroupFinish(taskId);
    }

    public WorkerTaskRela getByRelaId(String taskId) {
        return this.getOne(new QueryWrapper<WorkerTaskRela>()
                .lambda()
                .eq(WorkerTaskRela::getRelaId, taskId));
    }

    public WorkerTaskRela getByTaskId(String taskId) {
        return this.getOne(new QueryWrapper<WorkerTaskRela>()
                .lambda()
                .eq(WorkerTaskRela::getRelaId, taskId));
    }

    public boolean updateByRelaId(WorkerTaskRela workerTaskRela) {
        return this.update(workerTaskRela, new QueryWrapper<WorkerTaskRela>()
                .lambda()
                .eq(WorkerTaskRela::getRelaId, workerTaskRela.getRelaId()));
    }
}

