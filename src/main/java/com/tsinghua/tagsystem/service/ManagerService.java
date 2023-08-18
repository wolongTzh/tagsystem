package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.model.ManagerTaskSupervise;
import com.tsinghua.tagsystem.model.VO.ManagerTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ManagerService {

    public void createTask(CreateTaskParam param) throws IOException;

    public ManagerTasksVO getTasks(Integer userId);

    public ManagerTaskSupervise checkTask(String taskId) throws IOException;

    public boolean saveCheck(ManagerTaskSupervise managerTaskSupervise) throws IOException;
}
