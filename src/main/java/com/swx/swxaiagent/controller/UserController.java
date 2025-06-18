package com.swx.swxaiagent.controller;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.swxaiagent.web.common.BaseResponse;
import com.swx.swxaiagent.web.common.ResultUtils;
import com.swx.swxaiagent.web.exception.BusinessException;
import com.swx.swxaiagent.web.model.dto.user.UserLoginRequest;
import com.swx.swxaiagent.web.model.dto.user.UserRegisterRequest;
import com.swx.swxaiagent.web.model.entity.AppCallLog;
import com.swx.swxaiagent.web.model.entity.User;
import com.swx.swxaiagent.web.model.entity.UserSubscription;
import com.swx.swxaiagent.web.model.entity.VipLevel;
import com.swx.swxaiagent.web.model.enums.ErrorCode;
import com.swx.swxaiagent.web.model.vo.LoginUserVO;
import com.swx.swxaiagent.web.model.vo.MonthlyStatisticsVO;
import com.swx.swxaiagent.web.service.AppCallLogService;
import com.swx.swxaiagent.web.service.UserService;
import com.swx.swxaiagent.web.service.UserSubscriptionService;
import com.swx.swxaiagent.web.service.VipLevelService;
import com.swx.swxaiagent.web.utils.EmailUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.swx.swxaiagent.web.service.impl.UserServiceImpl.SALT;


/**
 * 用户接口
 *
 * @author swxswx
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private AppCallLogService appCallLogService;
    @Resource
    private UserSubscriptionService userSubscriptionService;
    @Resource
    private VipLevelService vipLevelService;
    @Resource
    private EmailUtil emailUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = userRegisterRequest.getEmail();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //验证码
        String verificationCode = userRegisterRequest.getVerificationCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }

        // 5. 验证码校验
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String redisKey = email;
        String storedCode = ops.get(redisKey);

        if (storedCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期，请重新获取");
        }
        if (!storedCode.equals(verificationCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword, email);
        return ResultUtils.success(result);
    }
    /**
     * 发送注册验证码
     */
    @PostMapping("/register/seedMail")
    public BaseResponse<Boolean> seedMail(@RequestBody String email) {
        boolean valid = emailUtil.isValid(email);
        if (!valid) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), "邮箱格式错误");
        }
        Boolean isSendEmail;
        try {
            isSendEmail = emailUtil.sendEmail(email);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "发送邮件失败");
        }
        return ResultUtils.success(isSendEmail);
    }



    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 生成APIKEY
     *
     * @param request
     * @return
     */
    @GetMapping("/generate/apikey")
    public BaseResponse<String> createApiKey(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        String ApiKey = DigestUtil.md5Hex(SALT + user.getUserAccount() + RandomUtil.randomNumbers(5));
        user.setApiKey(ApiKey);
        userService.updateById(user);
        return ResultUtils.success(ApiKey);
    }

    /**
     * 获取当前用户个人中心信息
     *
     * @param request
     * @return
     */
    @GetMapping("/get/user/info")
    public BaseResponse<User> getUserInfo(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(user);
    }

    /**
     * 用户订阅会员
     *
     * @param request
     * @return
     */
    @PostMapping("/subscribe")
    public BaseResponse<Boolean> userSubscribe(Long vipLevel, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        Boolean b = userService.userAddSubscription(user.getId(), vipLevel);
        return ResultUtils.success(b);
    }

    /**
     * 获取本月调用统计
     *
     * @param request
     * @return
     */
    @PostMapping("/get/user/statistics")
    public BaseResponse<MonthlyStatisticsVO> getStatisticsByMon(HttpServletRequest request) {
        MonthlyStatisticsVO monthlyStatisticsVO = new MonthlyStatisticsVO();
        User user = userService.getLoginUser(request);
        Long id = user.getId();

        LocalDateTime todayStart = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1).minusNanos(1);
        QueryWrapper<AppCallLog> todayCallQW = new QueryWrapper<>();
        todayCallQW.eq("userId", id)
                .ge("createTime", todayStart)
                .le("createTime", todayEnd);
        int todayCall = appCallLogService.list(todayCallQW).size();
        QueryWrapper<UserSubscription> userSubscriptionQueryWrapper = new QueryWrapper<>();
        userSubscriptionQueryWrapper.eq("userId", id)
                .eq("isActive", 1);

        UserSubscription userSubscription = userSubscriptionService.getOne(userSubscriptionQueryWrapper);
        int totalNumber = userSubscription.getCurrentUsage();
        Long vipLevelId = userSubscription.getVipLevelId();
        VipLevel vipLevel = vipLevelService.getById(vipLevelId);
        int monthlyLimit = vipLevel.getMonthlyLimit();
        int remainingAmount = monthlyLimit - totalNumber;

        //今日调用次数
        monthlyStatisticsVO.setTodayCall(todayCall);
        //总调用次数
        monthlyStatisticsVO.setTotalNumber(totalNumber);
        //剩余额度
        monthlyStatisticsVO.setRemainingAmount(remainingAmount);

        return ResultUtils.success(monthlyStatisticsVO);
    }


    // endregion


}
