package com.tsinghua.tagsystem;

import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.service.EvalDetailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class EvalDetailServiceTest {
    @Autowired
    EvalDetailService evalDetailService;

    @Test
    public void testGetEvalDetail() {
        // 调用getEvalDetail方法
        EvalDetailDecorate evalDetailDecorate = evalDetailService.getEvalDetail(2);
        // 打印结果
        log.info(evalDetailDecorate.toString());
    }

    @Test
    public void testUpdateEvalDetail() {
        // 初始化测试数据
        EvalDetailDecorate evalDetailDecorate = new EvalDetailDecorate();
        evalDetailDecorate.setEvalOverviewId(2);
        evalDetailDecorate.setEvalTestIds("1,2,3");
        evalDetailDecorate.setEvalAlgoIds("1,2");
        evalDetailDecorate.setEvalTestNames("测试集1,测试集2,测试集3");
        evalDetailDecorate.setEvalAlgoNames("模型1,模型2");

        // 调用updateEvalDetail方法
        int result = evalDetailService.updateEvalDetail(evalDetailDecorate);

        // 断言结果
        assertNotNull(result);
        assertEquals(1, result);
    }
}
