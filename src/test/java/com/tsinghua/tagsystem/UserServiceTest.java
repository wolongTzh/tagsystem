package com.tsinghua.tagsystem;

import com.alibaba.fastjson.JSONObject;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.model.params.UserParam;
import com.tsinghua.tagsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Test
    public void signupTest() throws IOException {
        userService.signup(UserParam.builder()
                        .name("测试人员")
                        .username("test")
                        .password("test")
                .build());
    }
    @Test
    public void loginTest() {
        TsUser tsUser = userService.login(UserParam.builder()
                .username("test")
                .password("test")
                .build());
        log.info(JSONObject.toJSONString(tsUser));
    }

    @Test
    public void getByIdTest() {
        TsUser tsUser = userService.getById(1);
        log.info(JSONObject.toJSONString(tsUser));
    }

    @Test
    public void getWorkersTest() {
        List<TsUser> tsUserList = userService.getWorkers();
        log.info(JSONObject.toJSONString(tsUserList));
    }
}
