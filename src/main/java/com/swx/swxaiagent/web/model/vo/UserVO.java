package com.swx.swxaiagent.web.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author   swxswx
 *   
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;


    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
    /**
     * 当前会员等级id 1-free，2-pro，3-ultra
     */
    private Long currentVipLevelId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}