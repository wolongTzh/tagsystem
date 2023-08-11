package com.tsinghua.tagsystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.model.CheckAtom;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.GetTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
public class ManagerServiceTest {

    @Autowired
    ManagerService managerService;
    @Test
    public void createTaskTest() throws IOException {
        File file = new File("./input.json");
        List<List<Integer>> members = new ArrayList<>();
        List<Integer> member1 = Arrays.asList(2, 3);
        List<Integer> member2 = Arrays.asList(4, 5);
        members.add(member1);
        members.add(member2);
        CreateTaskParam param = CreateTaskParam.builder()
                .file(file)
                .title("测试1")
                .members(members)
                .userId(1)
                .build();
        managerService.createTask(param);
    }
    @Test
    public void getTasksTest() {
        Integer userId = 1;
        GetTasksVO getTasksVO = managerService.getTasks(userId);
        log.info(JSON.toJSONString(getTasksVO));
    }

    @Test
    public void checkTasksTest() throws IOException {
        String taskId = "1b0519f1-4fcc-49c6-98de-1f93dc3c8a64";
        CheckTaskVO checkTaskVO = managerService.checkTask(taskId);
        log.info(JSON.toJSONString(checkTaskVO));
    }

    @Test
    public void finishCheckTasksTest() throws IOException {
        String taskId = "1b0519f1-4fcc-49c6-98de-1f93dc3c8a64";
        List<CheckAtom> checkAtomList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test.json")), CheckAtom.class);
        SaveCheckParam param = SaveCheckParam.builder()
                .taskId(taskId)
                .uncheckedNum(0)
                .checkList(checkAtomList)
                .build();
        managerService.saveCheck(param);
    }

    @Test
    public void saveCheckTasksTest() throws IOException {
        String taskId = "1b0519f1-4fcc-49c6-98de-1f93dc3c8a64";
        List<CheckAtom> checkAtomList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test.json")), CheckAtom.class);
        SaveCheckParam param = SaveCheckParam.builder()
                .taskId(taskId)
                .uncheckedNum(1)
                .checkList(checkAtomList)
                .build();
        managerService.saveCheck(param);
    }
}
