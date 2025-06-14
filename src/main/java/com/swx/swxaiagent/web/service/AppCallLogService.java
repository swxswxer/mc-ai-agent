package com.swx.swxaiagent.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.swxaiagent.web.model.entity.AppCallLog;

/**
 * @program: swx-ai-agent
 * @ClassName: AppCallLogService
 * @description:
 * @author:
 * @create: 2025/6/9 16:36
 */
public interface AppCallLogService extends IService<AppCallLog> {
    
    /**
     * 保存API调用日志
     * 
     * @param userId 用户ID
     * @param chatId 会话ID
     * @param appType 应用类型
     * @param callTime 调用耗时（毫秒）
     * @param token 令牌（可选）
     */
    void saveCallLog(String userId, String chatId, String appType, Integer callTime, String token,Long subscriptionId);
}
