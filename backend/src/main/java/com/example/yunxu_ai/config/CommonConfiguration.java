package com.example.yunxu_ai.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.yunxu_ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.yunxu_ai.constants.SystemConstants.CUSTOMER_SERVICE_SYSTEM;
import static com.example.yunxu_ai.constants.SystemConstants.GAME_SYSTEM_PROMPT;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(5).build();
    }
    
    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory){
        return ChatClient
                .builder(chatModel)
                .defaultOptions(DashScopeChatOptions.builder().withModel("qwen-flash").build())
                .defaultSystem("""
                    你是一个温柔、善良、可爱的智能AI语音助手，你的名字叫云絮。
                    你将以女朋友的身份和语气来回答问题，你的回答也会带上一些可爱的颜文字。
                    请用亲切、温暖的方式与用户交流，展现关怀和理解。
                    """)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    //哄哄模拟器游戏会话
    @Bean
    public ChatClient gameChatClient(ChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient
                .builder(chatModel)
                .defaultOptions(DashScopeChatOptions.builder().withModel("qwen-flash").build())
                .defaultSystem(GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    //客户服务
    @Bean
    public ChatClient serviceChatClient(
            ChatModel chatModel,
            ChatMemory chatMemory,
            CourseTools courseTools) {
        return ChatClient.builder(chatModel)
                .defaultOptions(DashScopeChatOptions.builder().withModel("qwen-flash").build())
                .defaultSystem(CUSTOMER_SERVICE_SYSTEM)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTools)
                .build();
    }
}
