package com.swx.swxaiagent.web.aspect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.swxaiagent.web.annotation.LogApiCall;
import com.swx.swxaiagent.web.exception.BusinessException;
import com.swx.swxaiagent.web.model.entity.User;
import com.swx.swxaiagent.web.model.entity.UserSubscription;
import com.swx.swxaiagent.web.model.entity.VipLevel;
import com.swx.swxaiagent.web.model.enums.ErrorCode;
import com.swx.swxaiagent.web.model.enums.VipLevelEnum;
import com.swx.swxaiagent.web.service.UserService;
import com.swx.swxaiagent.web.service.UserSubscriptionService;
import com.swx.swxaiagent.web.service.VipLevelService;
import com.swx.swxaiagent.web.service.impl.AppCallLogServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.UUID;

import static com.swx.swxaiagent.web.constant.UserConstant.USER_LOGIN_STATE;

/**
 * API调用日志切面
 * 
 * @author: 
 * @create: 2025/6/9
 */
@Slf4j
@Aspect
@Component
public class ApiCallLogAspect {

    @Autowired
    private AppCallLogServiceImpl appCallLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSubscriptionService userSubscriptionService;
    @Autowired
    private VipLevelService vipLevelService;

    @Around("@annotation(com.swx.swxaiagent.web.annotation.LogApiCall)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogApiCall logApiCall = method.getAnnotation(LogApiCall.class);
        String userId ;
        if (logApiCall.isApiKey()){
            // 获取请求信息
            HttpServletRequest request = getHttpServletRequest();
            String header = request.getHeader("X-API-Key");
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("apiKey",header);
            User user = userService.getOne(userQueryWrapper);
            userId = String.valueOf(user.getId());
        }else {
            // 获取请求信息
             userId = getUserId();
        }
        String chatId = getChatId(joinPoint);
        String appType = logApiCall.appType();
        String token = "TODO";

        //获取用户关联的订阅ID
        Long subscriptionId = getSubscriptionId(userId);
        //查询用户会员等级
        UserSubscription userSubscription = userSubscriptionService.getById(subscriptionId);
        Long vipLevelId = userSubscription.getVipLevelId();
        VipLevelEnum vipLevel = VipLevelEnum.getEnumByValue(String.valueOf(vipLevelId));
        //查询用户会员等级的月调用额度
        VipLevel vipLevelInfo = vipLevelService.getById(vipLevelId);
        //查询额度是否足够
        if ("CHAT".equals(appType)){
          if(userSubscription.getCurrentUsage() >= vipLevelInfo.getMonthlyLimit()){
              log.error("用户调用次数不足");
              return new BusinessException(ErrorCode.OPERATION_ERROR, "用户调用次数不足");
          }
        }else {
            if(userSubscription.getManusCurrentUsage() >= vipLevelInfo.getManusMonthlyLimit()){
                log.error("用户调用次数不足");
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户调用次数不足");
            }
        }

        log.info("开始执行API调用：{}, 用户ID：{}, 会话ID：{}, 应用类型：{}", 
                method.getName(), userId, chatId, appType);
        
        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            log.error("API调用异常：{}, 错误信息：{}", method.getName(), e.getMessage());
            throw e;
        } finally {
            // 记录调用日志
            long endTime = System.currentTimeMillis();
            int callTime = (int) (endTime - startTime);
            
            try {
                appCallLogService.saveCallLog(userId, chatId, appType, callTime, token,subscriptionId);
                log.info("API调用日志记录成功：{}, 耗时：{}ms", method.getName(), callTime);
                addUsage(subscriptionId, appType);
            } catch (Exception e) {
                log.error("记录API调用日志失败：{}", e.getMessage());
            }
        }
    }

    /**
     * 增加订阅表的调用记录
     * @param subscriptionId
     * @param appType
     */
    private void addUsage(Long subscriptionId, String appType) {
        UserSubscription userSubscription = userSubscriptionService.getById(subscriptionId);
        if ("CHAT".equals(appType)){
            userSubscription.setCurrentUsage(userSubscription.getCurrentUsage()+1);
        }else{
            userSubscription.setManusCurrentUsage(userSubscription.getManusCurrentUsage()+1);
        }
        userSubscriptionService.updateById(userSubscription);
    }

    /**
     * 获取用户ID（这里简单处理，实际项目中可能从JWT或Session中获取）
     */
    private String getUserId() {
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            // 先判断是否已登录
            Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
            User currentUser = (User) userObj;
            if (currentUser == null || currentUser.getId() == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
            }
            return   String.valueOf(currentUser.getId());
        }
        return "unknown";
    }
    
    /**
     * 获取会话ID
     */
    private String getChatId(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        
        // 查找chatId参数
        for (int i = 0; i < paramNames.length; i++) {
            if ("chatId".equals(paramNames[i]) && args[i] != null) {
                return args[i].toString();
            }
        }

        
        // 如果没有chatId参数，生成一个
        return "chat_" + UUID.randomUUID().toString().substring(0, 8);
    }


    /**
     * 获取订阅号ID
     */
    private Long getSubscriptionId(String userId) {
        User user = userService.getById(userId);
        Long currentSubscriptionId = user.getCurrentSubscriptionId();
        return currentSubscriptionId;
    }
    

    
    /**
     * 获取HttpServletRequest
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            log.warn("获取HttpServletRequest失败：{}", e.getMessage());
            return null;
        }
    }
} 