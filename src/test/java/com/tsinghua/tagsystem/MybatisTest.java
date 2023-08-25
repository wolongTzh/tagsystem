package com.tsinghua.tagsystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.TsUser;
import com.tsinghua.tagsystem.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class MybatisTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void selectTest() {
        List<TsUser> userList = userMapper.selectList(new QueryWrapper<TsUser>()
                .lambda()
                .eq(TsUser::getUserId, 1));
        System.out.println(System.currentTimeMillis());;
    }
}
