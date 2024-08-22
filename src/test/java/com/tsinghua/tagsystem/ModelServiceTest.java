package com.tsinghua.tagsystem;

import com.tsinghua.tagsystem.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
public class ModelServiceTest {

    @Autowired
    ModelService modelService;

    @Test
    public void trainTest() throws IOException, InterruptedException {
        modelService.train();
        Thread.sleep(10000);
    }
}
