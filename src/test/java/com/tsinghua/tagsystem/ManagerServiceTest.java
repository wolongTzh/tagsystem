package com.tsinghua.tagsystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.model.ManagerTaskSupervise;
import com.tsinghua.tagsystem.model.Member;
import com.tsinghua.tagsystem.model.Relation;
import com.tsinghua.tagsystem.model.VO.ManagerTasksVO;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
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
        List<List<TsUser>> taggingWorker = new ArrayList<>();
        List<TsUser> group1 = Arrays.asList(TsUser.builder()
                .name("远盈")
                .userId(2)
                .build(), TsUser.builder()
                .name("佳音")
                .userId(3)
                .build());
        List<TsUser> group2 = Arrays.asList(TsUser.builder()
                .name("浩天")
                .userId(4)
                .build(), TsUser.builder()
                .name("韵嘉")
                .userId(5)
                .build());
        taggingWorker.add(group1);
        taggingWorker.add(group2);
        Member members = Member.builder()
                .checkingWorker(TsUser.builder()
                        .name("测试人员")
                        .userId(6)
                        .build())
                .taggingWorker(taggingWorker)
                .build();
        CreateTaskParam param = CreateTaskParam.builder()
                .file(file)
                .title("20230818")
                .members(members)
                .userId(1)
                .build();
        managerService.createTask(param);
    }
    @Test
    public void getTasksTest() {
        Integer userId = 1;
        ManagerTasksVO managerTasksVO = managerService.getTasks(userId);
        log.info(JSON.toJSONString(managerTasksVO));
    }

    @Test
    public void checkTasksTest() throws IOException {
        String taskId = "4592e355-16c8-45fb-ad94-52acb6454e78";
        ManagerTaskSupervise managerTaskSupervise = managerService.checkTask(taskId);
        log.info(JSON.toJSONString(managerTaskSupervise));
    }

    @Test
    public void saveCheckTasksTest() throws IOException {
        String taskId = "4592e355-16c8-45fb-ad94-52acb6454e78";
        List<Relation> taskList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test.json")), Relation.class);
        ManagerTaskSupervise managerTaskSupervise = ManagerTaskSupervise.builder()
                .taskId(taskId)
                .taskList(taskList)
                .build();
        managerService.saveCheck(managerTaskSupervise);
    }
}
