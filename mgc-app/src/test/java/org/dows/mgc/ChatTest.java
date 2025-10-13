package org.dows.mgc;

import jakarta.annotation.Resource;
import org.dows.mgc.config.MgcConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {OpenAiChatAutoConfiguration.class, MgcConfiguration.class})
class ChatTest {

    @Resource
    ChatClient chatClient;

    @Test
    void testChat() {
        ChatResponse chatResponse = chatClient.prompt().user("学生基本信息姓名、性别(男/女)、生日、年龄、年纪、班级").call().chatResponse();
        System.out.println(chatResponse.getResult().getOutput().getText());
    }
}