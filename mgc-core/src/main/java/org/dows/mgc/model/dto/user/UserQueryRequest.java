package org.dows.mgc.model.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.mgc.common.PageRequest;

import java.io.Serializable;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户角色：user/admin
     */
    private String userRole;
    /**
     * 是否为VIP用户
     */
    private Boolean isVip;
}
