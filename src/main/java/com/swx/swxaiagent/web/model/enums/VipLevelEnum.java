package com.swx.swxaiagent.web.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: swx-ai-agent
 * @ClassName: VipLevel
 * @description:
 * @author:
 * @create: 2025/6/9 22:22
 */
public enum VipLevelEnum {
    FREE("FREE", "1"),
    PRO("PRO", "2"),
    ULTRA("ULTRA", "3");

    private final String text;

    private final String value;


    VipLevelEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static VipLevelEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (VipLevelEnum anEnum : VipLevelEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


}
