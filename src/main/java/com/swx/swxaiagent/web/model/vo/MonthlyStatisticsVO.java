package com.swx.swxaiagent.web.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: swx-ai-agent
 * @ClassName: MonthlyStatisticsVO
 * @description:月度统计VO
 * @author:
 * @create: 2025/6/9 21:17
 */
@Data
public class MonthlyStatisticsVO implements Serializable {
    /**
     * 今日调用次数
     */
   Integer todayCall;
    /**
     * 总调用次数
     */
   Integer totalNumber;
    /**
     * 剩余额度
     */
   Integer remainingAmount;

}
