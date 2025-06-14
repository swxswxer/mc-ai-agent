package com.swx.swxaiagent.ai.tool;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: swx-ai-agent
 * @ClassName: ToolRegistration
 * @description:
 * @author:
 * @create: 2025/6/8 01:59
 */
@Configuration
public class ToolRegistration {


    @Bean
    public ToolCallback[] allTools() {
        TerminateTool terminateTool = new TerminateTool();

        return ToolCallbacks.from(
                terminateTool
        );
    }
}

