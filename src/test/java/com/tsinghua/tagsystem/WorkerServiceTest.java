package com.tsinghua.tagsystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.model.CheckAtom;
import com.tsinghua.tagsystem.model.Relation;
import com.tsinghua.tagsystem.model.VO.CheckTaskVO;
import com.tsinghua.tagsystem.model.VO.WorkerTasksVO;
import com.tsinghua.tagsystem.model.WorkerTaskMsg;
import com.tsinghua.tagsystem.model.params.SaveCheckParam;
import com.tsinghua.tagsystem.service.WorkerService;
import com.tsinghua.tagsystem.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
        String taskId = "4b86605e-0a7c-4371-be91-ce1bc24a61fa";
        WorkerTaskMsg workerTaskMsg = workerService.tagTask(taskId);
        log.info(JSON.toJSONString(workerTaskMsg));
    }
    @Test
    public void finishTagTest() throws IOException {
        String taskId = "faa112e5-dcbc-4cb3-9725-a03ede4f1edb";
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
    @Test
    public void checkTasksTest() throws IOException {
        String taskId = "4592e355-16c8-45fb-ad94-52acb6454e78";
        CheckTaskVO checkTaskVO = workerService.checkTask(taskId);
        log.info(JSON.toJSONString(checkTaskVO));
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
        workerService.saveCheck(param);
    }
    @Test
    public void finishCheckTasksTest() throws IOException {
        String taskId = "4592e355-16c8-45fb-ad94-52acb6454e78";
        List<CheckAtom> checkAtomList = JSONObject.parseArray(JSONObject.toJSONString(CommonUtil.readJsonArray("./test.json")), CheckAtom.class);
        SaveCheckParam param = SaveCheckParam.builder()
                .taskId(taskId)
                .uncheckedNum(0)
                .checkList(checkAtomList)
                .build();
        workerService.saveCheck(param);
    }
}
