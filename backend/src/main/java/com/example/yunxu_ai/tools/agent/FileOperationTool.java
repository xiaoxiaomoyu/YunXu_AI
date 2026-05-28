package com.example.yunxu_ai.tools.agent;

import cn.hutool.core.io.FileUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.nio.file.Path;

/**
 * 文件操作工具类（提供文件读写功能）
 */
public class FileOperationTool {

    private final Path fileDir;

    public FileOperationTool(Path workspaceDir) {
        this.fileDir = workspaceDir.resolve("file").toAbsolutePath().normalize();
    }

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        try {
            Path filePath = AgentFileSupport.resolveSafePath(fileDir, fileName);
            return FileUtil.readUtf8String(filePath.toFile());
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file")
    public String writeFile(@ToolParam(description = "Name of the file to write") String fileName,
                            @ToolParam(description = "Content to write to the file") String content
    ) {
        try {
            Path filePath = AgentFileSupport.resolveSafePath(fileDir, fileName);
            FileUtil.mkdir(fileDir.toFile());
            FileUtil.writeUtf8String(content, filePath.toFile());
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}

