package org.dows.mgc.uim;

import java.time.LocalDateTime;

public interface User {

    public Long getId();

    public void setId(Long id);

    public String getUserAccount();

    public void setUserAccount(String userAccount);

    public String getUserPassword();

    public void setUserPassword(String userPassword);

    public String getUserName();

    public void setUserName(String userName);

    public String getUserAvatar();

    public void setUserAvatar(String userAvatar);

    public String getUserProfile();

    public void setUserProfile(String userProfile);

    public String getUserRole();

    public void setUserRole(String userRole);

    public LocalDateTime getEditTime();

    public void setEditTime(LocalDateTime editTime);

    public LocalDateTime getCreateTime();

    public void setCreateTime(LocalDateTime createTime);

    public LocalDateTime getUpdateTime();

    public void setUpdateTime(LocalDateTime updateTime);

    public Integer getIsDelete();

    public void setIsDelete(Integer isDelete);

    public LocalDateTime getVipExpireTime();

    public void setVipExpireTime(LocalDateTime vipExpireTime);

    public Long getVipNumber();

    public void setVipNumber(Long vipNumber);

    public String getShareCode();

    public void setShareCode(String shareCode);

    public Long getInviteUser();

    public void setInviteUser(Long inviteUser);

    public Boolean getIsVip();

    public void setIsVip(Boolean vip);
}
