package com.tsinghua.tagsystem;

import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.EvalOverviewDecorate;
import com.tsinghua.tagsystem.dao.entity.UploadTestDataParam;
import com.tsinghua.tagsystem.service.EvalOverviewService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
public class EvalOverviewServiceTest {
    @Autowired
    EvalOverviewService evalOverviewService;

    @Test
    public void testGetEvalOverview() {
        // 测试用例1：用户ID为1
        int userId1 = 1;
        List<EvalOverviewDecorate> result1 = evalOverviewService.getEvalOverview(userId1);
        System.out.println("测试结果1：" + result1);

        // 测试用例2：用户ID为2
        int userId2 = 2;
        List<EvalOverviewDecorate> result2 = evalOverviewService.getEvalOverview(userId2);
        System.out.println("测试结果2：" + result2);

        // 测试用例3：用户ID为3
        int userId3 = 3;
        List<EvalOverviewDecorate> result3 = evalOverviewService.getEvalOverview(userId3);
        System.out.println("测试结果3：" + result3);

        // 测试用例4：用户ID为4
        int userId4 = 4;
        List<EvalOverviewDecorate> result4 = evalOverviewService.getEvalOverview(userId4);
        System.out.println("测试结果4：" + result4);

        // 测试用例5：用户ID为5
        int userId5 = 5;
        List<EvalOverviewDecorate> result5 = evalOverviewService.getEvalOverview(userId5);
        System.out.println("测试结果5：" + result5);
    }

    @Test
    void addEvalOverview() {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName("测试任务1")
                .evalOverviewIntro("测试任务介绍1")
                .evalOverviewUserId(1)
                .evalOverviewUserName("user1")
                .build();
        int result = evalOverviewService.addEvalOverview(evalOverview);
        assertTrue(result > 0);
    }

    @Test
    void updateEvalOverview() {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName("测试任务1")
                .evalOverviewIntro("测试任务介绍1")
                .evalOverviewUserId(1)
                .evalOverviewUserName("user1")
                .build();
        int result = evalOverviewService.updateEvalOverview(evalOverview);
        assertEquals(1, result);

    }

    @Test
    void deleteEvalOverview() {
        int result = evalOverviewService.deleteEvalOverview(1);
        assertEquals(1, result);
    }
}
