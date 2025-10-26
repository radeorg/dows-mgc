package org.dows.mgc.chat;

import java.time.LocalDateTime;

public interface ChatHistory {

    public Long getId();

    public void setId(Long id);

    public String getMessage();

    public void setMessage(String message);

    public String getMessageType();

    public void setMessageType(String messageType);

    public Long getAppId();

    public void setAppId(Long appId);

    public Long getUserId();

    public void setUserId(Long userId);

    public LocalDateTime getCreateTime();

    public void setCreateTime(LocalDateTime createTime);

    public LocalDateTime getUpdateTime();

    public void setUpdateTime(LocalDateTime updateTime);

    public Integer getIsDelete();

    public void setIsDelete(Integer isDelete);
}
