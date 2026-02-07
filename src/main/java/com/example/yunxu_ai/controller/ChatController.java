package com.example.yunxu_ai.controller;

import com.example.yunxu_ai.enums.ChatType;
import com.example.yunxu_ai.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatHistoryService chatHistoryService;
    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")  //流式输出的时候.stream()要加上这个utf-8不然会乱码
    public Flux<String> chat(String prompt, String chatId) {   //用stream的时候string要加flux

        //创建一个聊天类型的枚举,通过创建枚举类型来替换硬编码的"chat"字符串
        //chatHistoryRepository.save("chat", chatId);
        // 使用Service方法
        //chatHistoryService.saveChat(chatId);
        // 或者明确指定类型
        chatHistoryService.saveChatHistory(ChatType.CHAT, chatId);

        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CONVERSATION_ID,chatId)) //会话IDkey
                .stream()
                .content();
    }

}
