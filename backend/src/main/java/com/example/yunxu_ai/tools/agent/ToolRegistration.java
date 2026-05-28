package com.example.yunxu_ai.tools.agent;

import com.example.yunxu_ai.config.YunxuAgentProperties;
import com.example.yunxu_ai.tools.CourseTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

/**
 * 集中的智能体工具注册类。
 */
@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    public ToolCallback[] allTools(YunxuAgentProperties properties, CourseTools courseTools) {
        Path workspaceDir = Path.of(properties.getWorkspaceDir());
        YunxuAgentProperties.Terminal terminal = properties.getTerminal();
        FileOperationTool fileOperationTool = new FileOperationTool(workspaceDir);
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool(workspaceDir);
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool(
                terminal.isEnabled(),
                Path.of(terminal.getAllowedWorkingDir()),
                terminal.getTimeoutSeconds(),
                terminal.getMaxOutputChars(),
                terminal.getBlockedCommands()
        );
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool(workspaceDir);
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                courseTools,
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool,
                terminateTool
        );
    }
}
