package com.example.yunxu_ai.config;

import com.example.yunxu_ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.yunxu_ai.constants.SystemConstants.CUSTOMER_SERVICE_SYSTEM;
import static com.example.yunxu_ai.constants.SystemConstants.GAME_SYSTEM_PROMPT;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(5).build();
        //return new InMemoryChatMemoryRepository();  //会话记忆功能
    }
    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory){
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo").build())
                .defaultSystem("""
                    你是一个温柔、善良、可爱的智能AI语音助手，你的名字叫云絮。
                    你将以女朋友的身份和语气来回答问题，你的回答也会带上一些可爱的颜文字。
                    请用亲切、温暖的方式与用户交流，展现关怀和理解。
                    """)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),  // 日志增强器
                        MessageChatMemoryAdvisor.builder(chatMemory).build()    //会话记忆功能
                )
                .build();
    }

    //哄哄模拟器游戏会话
    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()  // 修改这里，使用builder模式
                )
                .build();
    }

    //客户服务
    @Bean
    public ChatClient serviceChatClient(
            OpenAiChatModel model,
            ChatMemory chatMemory,
            CourseTools courseTools) {
        return ChatClient.builder(model)
                .defaultSystem(CUSTOMER_SERVICE_SYSTEM)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(), // CHAT MEMORY
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTools)
                .build();
    }

    //向量数据库,SimpleVectorStore是基于内存实现，是一个专门用来测试、学习的矢量数据库
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public ChatClient pdfChatClient(
            OpenAiChatModel model,
            ChatMemory chatMemory,
            VectorStore vectorStore) {
        return ChatClient.builder(model)
                .defaultSystem("请根据提供的上下文回答问题，不要自己猜测。")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(), // CHAT MEMORY
                        new SimpleLoggerAdvisor(),
                        QuestionAnswerAdvisor.builder(vectorStore) // 使用Builder模式
                                .searchRequest(SearchRequest.builder()
                                        .similarityThreshold(0.5d)
                                        .topK(2)
                                        .build())
                                .build()
                )
                .build();
    }
}
