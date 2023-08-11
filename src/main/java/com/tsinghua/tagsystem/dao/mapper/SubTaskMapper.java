package com.tsinghua.tagsystem.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsinghua.tagsystem.dao.entity.SubTask;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubTaskMapper extends BaseMapper<SubTask> {

    @Select("SELECT * \n" +
            "from sub_task\n" +
            "where sub_task.parent_id = #{taskId}"
    )
    List<SubTask> judgeTaskFinish(String taskId);
}
