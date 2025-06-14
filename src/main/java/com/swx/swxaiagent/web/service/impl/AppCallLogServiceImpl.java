package com.swx.swxaiagent.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.swxaiagent.web.mapper.AppCallLogMapper;
import com.swx.swxaiagent.web.mapper.UserMapper;
import com.swx.swxaiagent.web.model.entity.AppCallLog;
import com.swx.swxaiagent.web.model.entity.User;
import com.swx.swxaiagent.web.service.AppCallLogService;
import com.swx.swxaiagent.web.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: swx-ai-agent
 * @ClassName: AppCallLogServiceImpl
 * @description:
 * @author:
 * @create: 2025/6/9 16:37
 */
@Service
public class AppCallLogServiceImpl extends ServiceImpl<AppCallLogMapper, AppCallLog> implements AppCallLogService {

    /**
     * 保存API调用日志
     * 
     * @param userId 用户ID
     * @param chatId 会话ID
     * @param appType 应用类型
     * @param callTime 调用耗时（毫秒）
     * @param token 令牌（可选）
     */
    public void saveCallLog(String userId, String chatId, String appType, Integer callTime, String token,Long subscriptionId) {
        AppCallLog callLog = new AppCallLog();
        callLog.setUserId(userId);
        callLog.setChatId(chatId);
        callLog.setAppType(appType);
        callLog.setCallTime(callTime);
        callLog.setToken(token);
        callLog.setCreateTime(new Date());
        callLog.setIsDeleted(false);
        callLog.setSubscriptionId(subscriptionId);
        this.save(callLog);
    }

}
