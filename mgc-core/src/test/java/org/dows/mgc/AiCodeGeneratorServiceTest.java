package org.dows.mgc;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import org.dows.mgc.ai.AiCodeGeneratorService;
import org.dows.mgc.ai.model.HtmlCodeResult;
import org.dows.mgc.ai.model.MultiFileCodeResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Resource(name = "langchainOpenAiChatModel")
    private ChatModel chatModel;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("帮我生成一个网页页面，最多20行");
        assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个程序员鱼皮的留言板");
        assertNotNull(multiFileCode);
    }

    @Test
    void testChatModel() {
        try {
            String response = chatModel.chat("Hello, how are you?");
            System.out.println("✅ ChatModel 测试成功: " + response);
            assertNotNull(response);
        } catch (Exception e) {
            System.err.println("❌ ChatModel 测试失败: " + e.getMessage());
            fail("ChatModel test failed: " + e.getMessage());
        }
    }
}