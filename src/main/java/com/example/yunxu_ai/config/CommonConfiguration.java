package com.example.yunxu_ai.config;

import com.example.yunxu_ai.constants.SystemConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(5).build();
        //return new InMemoryChatMemoryRepository();  //会话记忆功能
    }
    @Bean
    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory){
        return ChatClient
                .builder(model)
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
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()  // 修改这里，使用builder模式
                )
                .build();
    }
}
