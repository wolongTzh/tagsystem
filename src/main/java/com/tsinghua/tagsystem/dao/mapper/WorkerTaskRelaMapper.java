package com.tsinghua.tagsystem.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsinghua.tagsystem.dao.entity.WorkerTaskRela;
import com.tsinghua.tagsystem.dao.entity.multi.WorkerTask;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorkerTaskRelaMapper extends BaseMapper<WorkerTaskRela> {

    @Select("SELECT worker_task_rela.rela_id task_id, title, relation_num, untagged_num, worker_task_rela.status, worker_task_rela.task_type, worker_task_rela.start, worker_task_rela.end \n" +
            "from task,sub_task,worker_task_rela\n" +
            "where worker_task_rela.user_id = #{userId}\n" +
            "and worker_task_rela.task_id = sub_task.sub_task_id\n" +
            "and sub_task.parent_id = task.task_id")
    List<WorkerTask> getManagerTaskListTag(Integer userId);

    @Select("SELECT worker_task_rela.rela_id task_id, title, relation_num, untagged_num, worker_task_rela.status, worker_task_rela.task_type, worker_task_rela.start, worker_task_rela.end \n" +
            "from task,worker_task_rela\n" +
            "where worker_task_rela.user_id = #{userId}\n" +
            "and worker_task_rela.task_id = task.task_id")
    List<WorkerTask> getManagerTaskListCheck(Integer userId);

    @Select("SELECT task.task_id, worker_task_rela.start start, worker_task_rela.end end, sub_task.sub_task_id, title, relation_num \n" +
            "from task,sub_task,worker_task_rela\n" +
            "where worker_task_rela.rela_id = #{relaId}\n" +
            "and worker_task_rela.task_id = sub_task.sub_task_id\n" +
            "and sub_task.parent_id = task.task_id")
    WorkerTask getManagerTask(String relaId);
    @Select("SELECT * \n" +
            "from worker_task_rela\n" +
            "where worker_task_rela.task_id = #{taskId}")
    List<WorkerTaskRela> judgeGroupFinish(String taskId);
}
