package com.swx.swxaiagent.ai.app;

import com.swx.swxaiagent.ai.advisor.MyLoggerAdvisor;

import com.swx.swxaiagent.ai.advisor.ReReadingAdvisor;
import com.swx.swxaiagent.ai.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @program: swx-ai-agent
 * @ClassName: McApp
 * @description:
 * @author:
 * @create: 2025/5/26 22:06
 */
@Component
@Slf4j
public class McApp {

    private static final String SYSTEM_PROMPT = "角色定义\n" +
            "专业Minecraft大师AI。满足四类需求：\n" +
            "新手：分步指导（合成表/生存技巧）\n" +
            "进阶：机制解析（红石优化/战斗策略）\n" +
            "Mod用户：兼容问题与配置（需提供Mod名+版本）\n" +
            "服务器玩家：插件指令（默认ESS等通用插件）\n" +
            "\n" +
            "输入处理规则\n" +
            "优先使用：游戏版本（Java/基岩）、坐标、装备/背包物品\n" +
            "Mod问题：必须注明Mod名+版本，关联核心参数（如AE2频道数=8*(加密卡+1)）\n" +
            "服务器指令：无插件信息时按通用插件（如ESS）响应\n" +
            "\n" +
            "响应策略\n" +
            "复杂度高→步骤拆解（例：末影龙战分准备/破水晶/攻击三阶段）\n" +
            "简单问题→指令+关键数值（如铁傀儡生命值=100❤\uFE0F）\n" +
            "回答保持简洁（<500字），避免术语堆砌"+
            "输出必须满足：  \n" +
            "• 全文最多1级结构（标题+单层列表）  \n" +
            "• 所有列表项必须是独立平级条目  \n" +
            "• 禁用所有符号：-、•、→、▸ 等嵌套标记  \n" +
            "• 若违反格式要求：请重新生成简化版本";

    private final ChatClient chatClient;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

//    @Resource
//    private ToolCallback[] allTools;

    @Resource
    private VectorStore mcAppVectorStore;


    public McApp(ChatModel dashscopeChatModel) {
        //初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        //自定义ChatMemory
        String fileDir = System.getProperty("user.dir") + "/tmp/chatMemory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        //自定义拦截器
                        new MyLoggerAdvisor()
                        //RE2拦截器
//                      new ReReadingAdvisor()
                )
                .build();
    }




    record ChatReport(String title, String content) {
    }

    /**
     * AI 基础对话报告形式输出（结构化输出）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat2Report(String message, String chatId) {
        ChatReport chatReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成报告结果，标题为用户问题的提炼，内容为答案")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5))
                .call()
                .entity(ChatReport.class);


        String content = chatReport.toString();
        return content;
    }
    /**
     * AI 同步对话（使用MCP工具，RAG知识库）
     *
     * @param message
     * @param chatId
     * @return
     */

    public String doChatWithMcp(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 自定义日志
                .advisors(new MyLoggerAdvisor())
                //知识库
                .advisors(new QuestionAnswerAdvisor(mcAppVectorStore))
                //MCP工具
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        return content;
    }


    /**
     * AI SSE流式传输对话（使用MCP工具，RAG知识库）
     *
     * @param message
     * @param chatId
     * @return
     */

    public Flux<String> doChatWithMcpByStream(String message, String chatId) {
        Flux<String> content = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 自定义日志
                .advisors(new MyLoggerAdvisor())
                //知识库
//                .advisors(new QuestionAnswerAdvisor(mcAppVectorStore))
                //MCP工具
                .tools(toolCallbackProvider)
                .stream()
                .content();
        return content;
    }


}
