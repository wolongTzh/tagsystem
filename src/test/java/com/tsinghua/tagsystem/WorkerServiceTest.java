package com.tsinghua.tagsystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.model.CheckAtom;
import com.tsinghua.tagsystem.model.Relation;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.GetTasksVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.WorkerTaskMsg;
import com.tsinghua.tagsystem.model.params.CreateTaskParam;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.ManagerService;
import com.tsinghua.tagsystem.service.WorkerService;
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
public class WorkerServiceTest {

    @Autowired
    WorkerService workerService;
    @Test
    public void getTasksTest() throws IOException {
        Integer userId = 3;
        WorkerTasksVO workerTasksVO = workerService.getTasks(userId);
        log.info(JSON.toJSONString(workerTasksVO));
    }
    @Test
    public void tagTasksTest() throws IOException {
        String taskId = "755c3d1b-7f36-49ce-8054-48c266d0631b";
        WorkerTaskMsg workerTaskMsg = workerService.tagTask(taskId);
        log.info(JSON.toJSONString(workerTaskMsg));
    }
    @Test
    public void finishTagTest() throws IOException {
        String taskId = "831b3ee9-f5a5-49d2-b8e8-ec30ba406d48";
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test2.json")), Relation.class);
        WorkerTaskMsg workerTaskMsg = WorkerTaskMsg.builder()
                .relationList(relationList)
                .taskId(taskId)
                .start(1)
                .end(2)
                .unTaggedNum(0)
                .build();
        boolean ret = workerService.saveTag(workerTaskMsg);
        log.info(JSON.toJSONString(ret));
    }
    @Test
    public void saveTagTest() throws IOException {
        String taskId = "e9db7f11-ed4f-43e6-a5bf-d247a1a4ff17";
        List<Relation> relationList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test2.json")), Relation.class);
        WorkerTaskMsg workerTaskMsg = WorkerTaskMsg.builder()
                .relationList(relationList)
                .taskId(taskId)
                .start(0)
                .end(1)
                .unTaggedNum(10)
                .build();
        boolean ret = workerService.saveTag(workerTaskMsg);
        log.info(JSON.toJSONString(ret));
    }
}
