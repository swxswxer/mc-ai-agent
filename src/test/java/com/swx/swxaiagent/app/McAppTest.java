package com.swx.swxaiagent.app;

import com.swx.swxaiagent.ai.app.McApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @program: swx-ai-agent
 * @ClassName: McAppTest
 * @description:
 * @author:
 * @create: 2025/5/26 22:19
 */
@SpringBootTest
class McAppTest {
@Resource
McApp mcApp;

    void doChat() {
        String chatId = "111222";
        String message = "如果是木制的，他有实际作用吗";
//        String answer = mcApp.doChat(message, chatId);
//        System.out.println(answer);


    }

    void doChat2Report() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有一个时运3的稿子有什么用？";
        String answer = mcApp.doChat2Report(message, chatId);
        System.out.println(answer);



    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "如何杀死末影龙";
//        String answer = mcApp.doChatWithRag(message, chatId);
//        System.out.println(answer);

    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有一个时运3的稿子有什么用？";
        String answer = mcApp.doChatWithMcp(message, chatId);
//        System.out.println(answer);
    }

}