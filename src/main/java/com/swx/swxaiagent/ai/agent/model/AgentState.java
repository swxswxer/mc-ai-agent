package com.swx.swxaiagent.ai.agent.model;

import lombok.Data;

/**
 * @program: swx-ai-agent
 * @ClassName: AgentState
 * @description:
 * @author:
 * @create: 2025/6/8 01:41
 */

public enum AgentState {
    /**
     * 空闲状态
     */
    IDLE,
    /**
     * 运行中状态
     */
    RUNNING,
    /**
     * 已完成状态
     */
    FINISHED,
    /**
     * 错误状态
     */
    ERROR,


}
