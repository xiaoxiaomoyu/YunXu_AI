package com.example.yunxu_ai.controller;

import com.example.yunxu_ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    private final ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(
            @RequestParam("prompt") String prompt,
            @RequestParam("chatId") String chatId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        // 1.保存会话id
        chatHistoryRepository.save("chat", chatId);
        // 2.请求模型
        if (files == null || files.isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(prompt, chatId);
        } else {
            // 有附件，多模态聊天
            return multiModalChat(prompt, chatId, files);
        }

    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 1.解析多媒体 - 使用Base64编码方式
        List<String> base64Images = files.stream()
                .map(file -> {
                    try {
                        byte[] bytes = file.getBytes();
                        return "data:" + file.getContentType() + ";base64," + 
                               java.util.Base64.getEncoder().encodeToString(bytes);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to process media file", e);
                    }
                })
                .toList();
        
        // 2.请求模型 - 使用文本方式描述图片
        StringBuilder promptBuilder = new StringBuilder(prompt);
        promptBuilder.append("\n\n用户上传了 ").append(files.size()).append(" 个文件:");
        for (int i = 0; i < files.size(); i++) {
            promptBuilder.append("\n文件").append(i + 1).append(": ").append(files.get(i).getOriginalFilename());
        }
        
        return chatClient.prompt()
                .user(promptBuilder.toString())
                .advisors(a -> a.param(CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
