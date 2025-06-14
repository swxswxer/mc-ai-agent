package com.swx.swxaiagent.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: swx-ai-agent
 * @ClassName: VipLevel
 * @description:
 * @author:
 * @create: 2025/6/9 22:43
 */
@Data
@TableName(value = "vip_level")
public class VipLevel implements Serializable {

    private Long id;

    private String levelName;
    /**
     * chat每月使用次数
     */
    private int monthlyLimit ;
    /**
     * manus每月使用次数
     */
    private int manusMonthlyLimit;
}
