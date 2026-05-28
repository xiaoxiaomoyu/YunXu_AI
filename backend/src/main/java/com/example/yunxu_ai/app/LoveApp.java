package com.example.yunxu_ai.app;

import com.example.yunxu_ai.advisor.MyLoggerAdvisor;
import com.example.yunxu_ai.chatmemory.FileBasedChatMemory;
import com.example.yunxu_ai.config.YunxuAgentProperties;
import com.example.yunxu_ai.rag.QueryRewriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;
    private final ObjectProvider<VectorStore> loveAppVectorStoreProvider;
    private final ObjectProvider<Advisor> loveAppRagCloudAdvisorProvider;
    private final ObjectProvider<VectorStore> pgVectorVectorStoreProvider;
    private final ObjectProvider<QueryRewriter> queryRewriterProvider;
    private final ObjectProvider<ToolCallback[]> allToolsProvider;
    private final ObjectProvider<ToolCallbackProvider> toolCallbackProvider;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    public LoveApp(
            ChatModel dashscopeChatModel,
            YunxuAgentProperties properties,
            @Qualifier("loveAppVectorStore") ObjectProvider<VectorStore> loveAppVectorStoreProvider,
            @Qualifier("loveAppRagCloudAdvisor") ObjectProvider<Advisor> loveAppRagCloudAdvisorProvider,
            @Qualifier("pgVectorVectorStore") ObjectProvider<VectorStore> pgVectorVectorStoreProvider,
            ObjectProvider<QueryRewriter> queryRewriterProvider,
            ObjectProvider<ToolCallback[]> allToolsProvider,
            ObjectProvider<ToolCallbackProvider> toolCallbackProvider) {
        ChatMemory chatMemory = createChatMemory(properties);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new MyLoggerAdvisor()
                )
                .build();
        this.loveAppVectorStoreProvider = loveAppVectorStoreProvider;
        this.loveAppRagCloudAdvisorProvider = loveAppRagCloudAdvisorProvider;
        this.pgVectorVectorStoreProvider = pgVectorVectorStoreProvider;
        this.queryRewriterProvider = queryRewriterProvider;
        this.allToolsProvider = allToolsProvider;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    private ChatMemory createChatMemory(YunxuAgentProperties properties) {
        if (properties.getLoveApp().isFileMemoryEnabled()) {
            return new FileBasedChatMemory(properties.getLoveApp().getFileMemoryDir());
        }
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 基础对话（支持多轮对话记忆，SSE 流式传输）
     *
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        VectorStore loveAppVectorStore = loveAppVectorStoreProvider.getIfAvailable();
        QueryRewriter queryRewriter = queryRewriterProvider.getIfAvailable();
        if (loveAppVectorStore != null && queryRewriter != null) {
            return chatClient
                    .prompt()
                    .user(queryRewriter.doQueryRewrite(message))
                    .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                    .advisors(QuestionAnswerAdvisor.builder(loveAppVectorStore).build())
                    .stream()
                    .content()
                    .concatWithValues("[DONE]");
        }
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content()
                .concatWithValues("[DONE]");
    }

    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * AI 恋爱报告功能（实战结构化输出）
     *
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    // AI 恋爱知识库问答功能

    /**
     * 和 RAG 知识库进行对话
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        VectorStore loveAppVectorStore = loveAppVectorStoreProvider.getIfAvailable();
        QueryRewriter queryRewriter = queryRewriterProvider.getIfAvailable();
        if (loveAppVectorStore == null || queryRewriter == null) {
            return doChat(message, chatId);
        }
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                // 使用改写后的查询
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用 RAG 知识库问答
                .advisors(QuestionAnswerAdvisor.builder(loveAppVectorStore).build())
                // 应用 RAG 检索增强服务（基于云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用 RAG 检索增强服务（基于 PgVector 向量存储）
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义的 RAG 检索增强服务（文档查询器 + 上下文增强器）
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
//                                loveAppVectorStore, "单身"
//                        )
//                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 恋爱报告功能（支持调用工具）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        ToolCallback[] allTools = allToolsProvider.getIfAvailable();
        if (allTools == null || allTools.length == 0) {
            return doChat(message, chatId);
        }
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI 调用 MCP 服务

    /**
     * AI 恋爱报告功能（调用 MCP 服务）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(String message, String chatId) {
        ToolCallbackProvider provider = toolCallbackProvider.getIfAvailable();
        if (provider == null) {
            return "MCP 工具未启用。请配置 YUNXU_MCP_CLIENT_ENABLED=true 并提供 MCP 服务。";
        }
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(provider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}

