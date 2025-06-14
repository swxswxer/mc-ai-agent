package com.swx.swxaiagent.agent;

import com.swx.swxaiagent.ai.agent.McManus;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: swx-ai-agent
 * @ClassName: McManusTest
 * @description:
 * @author:
 * @create: 2025/6/8 02:13
 */
@SpringBootTest
class McManusTest {
    @Resource
    private McManus mcManus;
    @Test
    void run(){
        String userPrompt = "你好，请告诉我Minecraft中如何制作一把钻石剑";
        String answer = mcManus.run(userPrompt);
        System.out.println("================================");
        System.out.println(answer);
        Assertions.assertNotNull(answer);
    }

}