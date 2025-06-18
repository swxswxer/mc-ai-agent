package com.swx.swxaiagent.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.swx.swxaiagent.web.exception.BusinessException;
import com.swx.swxaiagent.web.mapper.UserMapper;
import com.swx.swxaiagent.web.model.entity.User;
import com.swx.swxaiagent.web.model.entity.UserSubscription;
import com.swx.swxaiagent.web.model.enums.ErrorCode;
import com.swx.swxaiagent.web.model.enums.UserRoleEnum;
import com.swx.swxaiagent.web.model.enums.VipLevelEnum;
import com.swx.swxaiagent.web.model.vo.LoginUserVO;
import com.swx.swxaiagent.web.model.vo.UserVO;
import com.swx.swxaiagent.web.service.UserService;
import com.swx.swxaiagent.web.service.UserSubscriptionService;
import com.swx.swxaiagent.web.utils.EmailUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.swx.swxaiagent.web.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户服务实现
 *
 * @author swxswx
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserSubscriptionService userSubscriptionService;
    @Resource
    private EmailUtil emailUtil;
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "swx";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String email) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //邮箱校验
        if (!emailUtil.isValid(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式无效");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapperAccount = new QueryWrapper<>();
            queryWrapperAccount.eq("userAccount", userAccount);
            long countAccount = this.baseMapper.selectCount(queryWrapperAccount);
            if (countAccount > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 邮箱不能重复
            QueryWrapper<User> queryWrapperEmail = new QueryWrapper<>();
            queryWrapperEmail.eq("email", email);
            long countEmail = this.baseMapper.selectCount(queryWrapperEmail);
            if (countEmail > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已被注册了");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

//            userSubscriptionService

            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setEmail(email);
            user.setUserRole(UserRoleEnum.USER.getValue());
            //设置用户会员等级为FREE
            user.setCurrentVipLevelId(Long.valueOf(VipLevelEnum.FREE.getValue()));
            //设置用户默认头像
            user.setUserAvatar("https://avatars.githubusercontent.com/u/120923708?v=4&size=64");
            //默认名称为用户名
            user.setUserName(userAccount);
            this.save(user);
            //创建Free的订阅记录
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUserId(user.getId());
            userSubscription.setVipLevelId(Long.valueOf(VipLevelEnum.FREE.getValue()));
            userSubscriptionService.save(userSubscription);
            //设置订阅记录ID
            user.setCurrentSubscriptionId(userSubscription.getId());
            //重新更新
            boolean saveResult = this.updateById(user);

            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        log.info("登陆用户：" + currentUser);
        return currentUser;
    }


    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public Boolean userAddSubscription(Long userId, Long vipLevel) {
        User user = this.getById(userId);
        if (user.getCurrentVipLevelId() == vipLevel){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"你已经是"+VipLevelEnum.getEnumByValue(String.valueOf(vipLevel))+"用户了");
        }
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();   // 当前时间
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserId(userId);
        userSubscription.setVipLevelId(vipLevel);
        userSubscription.setStartTime(now);
        userSubscription.setEndTime(now.plusMonths(1));
        userSubscription.setIsActive(Boolean.TRUE);
        //设置其他订阅状态为失效
        QueryWrapper<UserSubscription> qw = new QueryWrapper<>();
        qw.eq("userId",userId).eq("isActive",1);
        UserSubscription oldActiveSubscription = userSubscriptionService.getOne(qw);
        oldActiveSubscription.setIsActive(Boolean.FALSE);
        userSubscriptionService.updateById(oldActiveSubscription);
        //插入新订阅
        userSubscriptionService.save(userSubscription);
        //修改user表关联的订阅ID

        user.setCurrentSubscriptionId(userSubscription.getId());
        user.setCurrentVipLevelId(vipLevel);
        boolean updateById = this.updateById(user);
        return updateById;
    }


}
