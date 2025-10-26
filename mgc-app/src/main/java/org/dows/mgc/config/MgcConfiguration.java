package org.dows.mgc.config;

import org.dows.mgc.MgcProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MgcProperties.class})
public class MgcConfiguration {


    @Bean
    OpenAiChatModel springAiChatModel(MgcProperties properties) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(System.getProperty("DEEPSEEK_API_KEY"))
                .baseUrl("https://api.deepseek.com")
                .build();
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("deepseek-chat")
                .temperature(0.7)
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }
    @Bean
    public ChatClient chatClient(ChatModel springAiChatModel) {
        ChatClient.Builder builder = ChatClient.builder(springAiChatModel)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build());
        return builder.build();
    }

//    @Bean
//    public AiEntityGenerator mgcService(ChatClient chatClient, ModelWebService modelWebService, MgcProperties properties) {
//        return new AiEntityGenerator(chatClient, modelWebService, properties);
//    }
}
