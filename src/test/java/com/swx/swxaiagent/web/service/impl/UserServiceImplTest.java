package com.swx.swxaiagent.web.service.impl;

import com.swx.swxaiagent.web.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: swx-ai-agent
 * @ClassName: UserServiceImplTest
 * @description:
 * @author:
 * @create: 2025/6/11 21:32
 */
@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;
    @Test
    void test(){
        Boolean b = userService.userAddSubscription(5L, 2L);


    }

}