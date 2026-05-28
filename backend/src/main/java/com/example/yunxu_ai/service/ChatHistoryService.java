package com.example.yunxu_ai.service;
//创建一个聊天类型的枚举,通过创建枚举类型来替换硬编码的"chat"字符串,ChatController.java中的
import com.example.yunxu_ai.enums.ChatType;
import com.example.yunxu_ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {
    private final ChatHistoryRepository chatHistoryRepository;

    public void saveChatHistory(ChatType chatType, String chatId) {
        chatHistoryRepository.save(chatType.getValue(), chatId);
    }

    // 便捷方法
    public void saveChat(String chatId) {
        saveChatHistory(ChatType.CHAT, chatId);
    }

    public void saveQuestion(String chatId) {
        saveChatHistory(ChatType.QUESTION, chatId);
    }
}