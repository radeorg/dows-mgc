package org.dows.mgc.config;

import org.dows.mgc.MgcProperties;
import org.dows.mgc.service.MgcService;
import org.dows.mgc.service.impl.MgcServiceImpl;
import org.dows.mgc.sql.web.ModelWebService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MgcProperties.class})
public class MgcConfiguration {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build());
        return builder.build();
    }

    @Bean
    public MgcService mgcService(ChatClient chatClient, ModelWebService modelWebService, MgcProperties properties) {
        return new MgcServiceImpl(chatClient, modelWebService, properties);
    }
}
