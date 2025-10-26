package org.dows.mgc.chat;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.dows.mgc.model.dto.chathistory.ChatHistoryQueryRequest;
import org.dows.mgc.uim.User;

import java.time.LocalDateTime;

public interface ChatHistoryAdaptor {
    /**
     * 添加对话历史
     *
     * @param appId
     * @param message
     * @param messageType
     * @param userId
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用ID删除对话历史
     *
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);



    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
