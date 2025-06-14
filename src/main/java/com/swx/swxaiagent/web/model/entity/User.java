package com.swx.swxaiagent.web.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 当前会员等级id
     */
    private Long currentVipLevelId;
    /**
     * 当前有效的订阅id（可能为NULL，表示没有订阅，即free）
     */
    private Long currentSubscriptionId;
    /**
     * 用户角色: user, admin
     */
    private String userRole;
    /**
     * 邮箱
     */
    private String email;


    /**
     * 密码
     */
    private String userPassword;

    /**
     * apiKey
     */
    private String apiKey;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}