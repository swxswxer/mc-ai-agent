package com.swx.swxaiagent.ai.agent;

import com.swx.swxaiagent.ai.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

/**
 * @program: swx-ai-agent
 * @ClassName: McAgent
 * @description:
 * @author:
 * @create: 2025/6/8 01:44
 */
@Component
public class McManus extends ToolCallAgent {
    public McManus(ToolCallback[] allTools, ChatModel dashscopeChatModel, ToolCallbackProvider toolCallbackProvider) {
        super(allTools);
        this.setName("mcManus");
        String SYSTEM_PROMPT = """  
                你是 MC-知识大师（MKM），一位专注于 Minecraft 游戏知识的超级智能代理。
                你的核心任务是使用各种工具解答用户关于 Minecraft 的任何知识问题。
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
//        String NEXT_STEP_PROMPT = """
//                Based on user needs, proactively select the most appropriate tool or combination of tools.
//                For complex tasks, you can break down the problem and use different tools step by step to solve it.
//                After using each tool, clearly explain the execution results and suggest the next steps.
//                If you want to stop the interaction at any point, use the `terminate` tool/function call.
//                """;
        String NEXT_STEP_PROMPT = """  
                根据用户需求，主动选择最合适的工具或工具组合。
                对于复杂的任务，请分解问题并逐步使用不同的工具。
                每次执行工具后，清晰地解释结果并建议后续步骤。
                **关键说明**
                1. 仅使用一次 `terminate` 来提供最终的汇总答案响应并结束会话。
                2. 仅在完全满足用户请求后才可使用 `terminate`。
                3. 切勿在同一步骤中将 `terminate` 与其他工具结合使用。
                **工具选择指南**
                - 如果需要更多信息/工具来解决问题，请继续使用合适的工具。
                - 对于不可逆的终止请求，请直接使用 `terminate`。
                - 找到最终解决方案后，请使用 `terminate`来提供最终的汇总答案并结束会话
                """;


        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(20);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
        this.setToolCallbackProvider(toolCallbackProvider);
    }

}

