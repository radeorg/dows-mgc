//package org.dows.mgc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.model.chat.StreamingChatModel;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
//
//@Configuration
//public class LangChain4jConfig {
//
//    @Bean
//    public ChatModel langchainOpenAiChatModel() {
//        return OpenAiChatModel.builder()
//                .baseUrl("https://api-inference.modelscope.cn/v1")
//                .apiKey("ms-96b01f31-0826-4fd1-96a8-8e7214e36f26")
//                .modelName("Qwen/Qwen2.5-Coder-32B-Instruct")
//                .logRequests(true)
//                .logResponses(true)
//                .maxTokens(8192)
//                .build();
//    }
//
//    @Bean
//    public StreamingChatModel langchainStreamingChatModel() {
//        return OpenAiStreamingChatModel.builder()
//                .baseUrl("https://api-inference.modelscope.cn/v1")
//                .apiKey("ms-96b01f31-0826-4fd1-96a8-8e7214e36f26")
//                .modelName("Qwen/Qwen3-235B-A22B-Instruct-2507")
//                .logRequests(true)
//                .logResponses(true)
//                .maxTokens(8192)
//                .build();
//    }
//}