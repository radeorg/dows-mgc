package org.dows.mgc.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.MgcProperties;
import org.dows.mgc.loader.compiler.DynamicCompiler;
import org.dows.mgc.sql.web.ModelWebService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiEntityGenerator {

    private final ChatClient chatClient;
    private final ModelWebService modelWebService;
    private final MgcProperties properties;

    public Flux<String> buildRequirement(String text) {
        return chatClient.prompt()
                .system(properties.getRequirementBuilder())
                .user(text)
                .stream()
                .content();
    }

    public Flux<String> buildClass(String text) {
        return chatClient.prompt()
                .system(properties.getClassBuilder())
                .user(text)
                .stream()
                .content();
    }

    public Map<String, Object> buildWeb(String text) {
        Map<String, Object> result = new HashMap<>();
        String id = UUID.randomUUID().toString();

        if (text.indexOf("<think>") > 0)
            text = text.substring(text.indexOf("</think>") + 8);
        if (text.contains("```java"))
            text = text.substring(text.indexOf("```java") + 7, text.lastIndexOf("```"));

        result.put("javaSourceCode", text);

        Class<?> clazz;
        try {
            clazz = DynamicCompiler.compileAndLoad(text);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        modelWebService.build(id, clazz);

        result.put("id", id);
        return result;
    }
}
