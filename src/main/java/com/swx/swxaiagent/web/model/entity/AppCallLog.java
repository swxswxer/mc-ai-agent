package com.swx.swxaiagent.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: swx-ai-agent
 * @ClassName: AppCallLog
 * @description:
 * @author:
 * @create: 2025/6/9 16:33
 */
@Data
@TableName(value = "app_call_log")
public class AppCallLog implements Serializable {
    private Long id;

    private String userId;
    /**
     * 关联的订阅记录ID
     */
    private Long subscriptionId;

    private String chatId;

    private String appType;

    private Integer callTime;

    private String token;

    private Date createTime;

    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}


