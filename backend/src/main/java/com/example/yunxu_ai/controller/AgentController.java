package com.example.yunxu_ai.controller;

import com.example.yunxu_ai.agent.YuManus;
import com.example.yunxu_ai.app.LoveApp;
import com.example.yunxu_ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class AgentController {

    private final LoveApp loveApp;
    private final ToolCallback[] allTools;
    private final ChatModel dashscopeChatModel;
    private final ChatHistoryRepository chatHistoryRepository;

    @GetMapping("/love-app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        chatHistoryRepository.save("love", chatId);
        return loveApp.doChatWithRag(message, chatId);
    }

    @GetMapping(value = "/love-app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSse(String message, String chatId) {
        chatHistoryRepository.save("love", chatId);
        return loveApp.doChatByStream(message, chatId);
    }

    @GetMapping(value = "/manus/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithManus(String message) {
        YuManus yuManus = new YuManus(allTools, dashscopeChatModel);
        return yuManus.runStream(message);
    }
}
