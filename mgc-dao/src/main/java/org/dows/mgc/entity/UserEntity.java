package org.dows.mgc.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dows.mgc.uim.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class UserEntity implements User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 账号
     */
    @Column("userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @Column("userPassword")
    private String userPassword;

    /**
     * 用户昵称
     */
    @Column("userName")
    private String userName;

    /**
     * 用户头像
     */
    @Column("userAvatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Column("userProfile")
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    @Column("userRole")
    private String userRole;

    /**
     * 编辑时间
     */
    @Column("editTime")
    private LocalDateTime editTime;

    /**
     * 创建时间
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(value = "isDelete", isLogicDelete = true)
    private Integer isDelete;

    /**
     * 会员过期时间
     */
    @Column("vipExpireTime")
    private LocalDateTime vipExpireTime;


    /**
     * 会员编号
     */
    @Column("vipNumber")
    private Long vipNumber;

    /**
     * 分享码
     */
    @Column("shareCode")
    private String shareCode;

    /**
     * 邀请用户 id
     */
    @Column("inviteUser")
    private Long inviteUser;

    /**
     * VIP状态缓存（0-否，1-是）
     */
    @Column("isVip")
    private Boolean isVip;

}
