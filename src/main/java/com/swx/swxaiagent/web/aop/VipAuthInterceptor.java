package com.swx.swxaiagent.web.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.swx.swxaiagent.web.annotation.VipAuthCheck;
import com.swx.swxaiagent.web.exception.BusinessException;
import com.swx.swxaiagent.web.model.entity.User;
import com.swx.swxaiagent.web.model.enums.ErrorCode;
import com.swx.swxaiagent.web.model.enums.VipLevelEnum;
import com.swx.swxaiagent.web.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限校验 AOP
 *
 * @author swxswxer
 */
@Aspect
@Component
public class VipAuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, VipAuthCheck authCheck) throws Throwable {
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User user = userService.getLoginUser(request);
        // 拥有任意权限即通过
        if (CollectionUtils.isNotEmpty(anyRole)) {
            String vipLevel = String.valueOf(user.getCurrentVipLevelId());
            if (!anyRole.contains(VipLevelEnum.getEnumByValue(vipLevel).getText())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 必须有所有权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            String vipLevel = String.valueOf(user.getCurrentVipLevelId());
            if (!mustRole.equals(VipLevelEnum.getEnumByValue(vipLevel).getText())) {
               throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

