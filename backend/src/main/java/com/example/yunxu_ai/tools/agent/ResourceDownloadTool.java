package com.example.yunxu_ai.tools.agent;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.nio.file.Path;

/**
 * 资源下载工具
 */
public class ResourceDownloadTool {

    private final Path downloadDir;

    public ResourceDownloadTool(Path workspaceDir) {
        this.downloadDir = workspaceDir.resolve("download").toAbsolutePath().normalize();
    }

    @Tool(description = "Download a resource from a given URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url, @ToolParam(description = "Name of the file to save the downloaded resource") String fileName) {
        try {
            Path filePath = AgentFileSupport.resolveSafePath(downloadDir, fileName);
            FileUtil.mkdir(downloadDir.toFile());
            HttpUtil.downloadFile(url, new File(filePath.toString()));
            return "Resource downloaded successfully to: " + filePath;
        } catch (Exception e) {
            return "Error downloading resource: " + e.getMessage();
        }
    }
}

