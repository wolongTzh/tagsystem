package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.WorkerTaskMsg;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WorkerService {

    public WorkerTasksVO getTasks(Integer userId);

    public WorkerTaskMsg tagTask(String taskId) throws IOException;

    public boolean saveTag(WorkerTaskMsg param) throws IOException;

    public CheckTaskVO checkTask(String taskId) throws IOException;

    public boolean saveCheck(SaveCheckParam param) throws IOException;
}
