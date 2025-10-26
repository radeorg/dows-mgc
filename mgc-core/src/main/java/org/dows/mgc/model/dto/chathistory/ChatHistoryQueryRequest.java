package org.dows.mgc.model.dto.chathistory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.mgc.common.PageRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 消息类型（user/ai）
     */
    private String messageType;
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 创建用户id
     */
    private Long userId;
    /**
     * 游标查询 - 最后一条记录的创建时间
     * 用于分页查询，获取早于此时间的记录
     * 游标查询是要查询小于上一个查询结果的最后一条记录的创建时间的对象
     */
    private LocalDateTime lastCreateTime;
}

