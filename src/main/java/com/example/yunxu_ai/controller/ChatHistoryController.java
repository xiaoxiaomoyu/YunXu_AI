package com.example.yunxu_ai.controller;

import com.example.yunxu_ai.entity.vo.MessageVO;
import com.example.yunxu_ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatMemory chatMemory;

    //查询会话记录列表,请求方式GET,请求路径/ai/history/{type},请求参数type：业务类型,返回值["1241"，"1246"，"1248"]
    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatIds(type);
    }
    //查询会话记录详情,请求方式GET,请求路径/ai/history/{type}/{chatId},请求参数type:业务类型 chatId:会话id,返回值[{"role":"user","content":""}]
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if(messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}