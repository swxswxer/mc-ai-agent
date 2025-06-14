package com.swx.swxaiagent.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: swx-ai-agent
 * @ClassName: UserSubscription
 * @description:
 * @author:
 * @create: 2025/6/9 22:31
 */
@Data
@TableName(value = "user_subscription")
public class UserSubscription implements Serializable {
    /**
     * 订阅记录ID
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * vip等级id
     */
    private Long vipLevelId;
    /**
     * 订阅开始时间
     */
    private LocalDateTime startTime;
    /**
     * 订阅结束时间（free用户为NULL表示永久）
     */
    private LocalDateTime endTime;
    /**
     * 当前订阅周期内已使用次数
     */
    private int currentUsage;
    /**
     * 当订阅周期内Manus已使用次数
     */
    private int manusCurrentUsage;
    /**
     * 是否有效（1-有效，0-无效）
     */
    private Boolean isActive;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
