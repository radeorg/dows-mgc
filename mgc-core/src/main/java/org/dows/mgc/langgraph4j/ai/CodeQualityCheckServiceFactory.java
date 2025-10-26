package org.dows.mgc.langgraph4j.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CodeQualityCheckServiceFactory {

    @Resource(name = "langchainOpenAiChatModel")
    private ChatModel chatModel;

    public CodeQualityCheckService createService() {
        return AiServices.builder(CodeQualityCheckService.class)
                .chatModel(chatModel)
                .build();
    }
}