package com.swx.swxaiagent.web.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: swx-ai-agent
 * @ClassName: EmailUtilTest
 * @description:
 * @author:
 * @create: 2025/6/18 22:30
 */
@SpringBootTest
class EmailUtilTest {
    @Resource
    private EmailUtil emailUtil;

    @Test
    void sendEmail() throws MessagingException {
        Boolean b = emailUtil.sendEmail("a913685201@163.com");
        System.out.println(b);
    }

}