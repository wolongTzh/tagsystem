package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.model.ManagerTaskSupervise;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.GetTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ManagerService {

    public void createTask(CreateTaskParam param) throws IOException;

    public GetTasksVO getTasks(Integer userId);

    public ManagerTaskSupervise checkTask(String taskId) throws IOException;

    public boolean saveCheck(ManagerTaskSupervise managerTaskSupervise) throws IOException;
}
