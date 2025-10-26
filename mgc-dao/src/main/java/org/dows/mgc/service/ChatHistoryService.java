package org.dows.mgc.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.dows.mgc.chat.ChatHistory;
import org.dows.mgc.chat.ChatHistoryAdaptor;
import org.dows.mgc.entity.ChatHistoryEntity;
import org.dows.mgc.model.dto.chathistory.ChatHistoryQueryRequest;
import org.dows.mgc.uim.User;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 */
public interface ChatHistoryService extends IService<ChatHistoryEntity>, ChatHistoryAdaptor {
    /**
     * 构造查询请求
     *
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistoryEntity> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);


}
