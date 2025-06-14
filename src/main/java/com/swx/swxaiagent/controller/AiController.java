package com.swx.swxaiagent.controller;

import com.swx.swxaiagent.ai.agent.McManus;
import com.swx.swxaiagent.ai.app.McApp;
import com.swx.swxaiagent.web.annotation.LogApiCall;
import com.swx.swxaiagent.web.annotation.VipAuthCheck;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * @program: swx-ai-agent
 * @ClassName: AiController
 * @description:
 * @author:
 * @create: 2025/6/8 16:55
 */

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;
    @Resource
    private McApp mcApp;

    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @LogApiCall(appType = "AGENT", description = "智能体对话")
    @GetMapping("/agent")
    @VipAuthCheck(mustRole = "ULTRA")
    public SseEmitter doAgentWithSSE(String message) {
        McManus mcManus = new McManus(allTools, dashscopeChatModel,toolCallbackProvider);
        return mcManus.runStream(message);
    }

    /**
     * 流式调用基础对话
     *
     * @param message
     * @return
     */
    @LogApiCall(appType = "CHAT", description = "基础对话")
    @GetMapping(value = "/chat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSSE(String message, String chatId) {

        return mcApp.doChatWithMcpByStream(message, chatId);

    }

    /**
     * 流式调用基础对话（APIKEY）
     *
     * @param message
     * @return
     */
    @LogApiCall(appType = "CHAT", description = "基础对话",isApiKey = true)
    @GetMapping(value = "/chat/apikey",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSSEByApiKey(String message, String chatId) {

        return mcApp.doChatWithMcpByStream(message, chatId);

    }



}
