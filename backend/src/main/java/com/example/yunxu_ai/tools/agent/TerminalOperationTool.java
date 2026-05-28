package com.example.yunxu_ai.tools.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 终端操作工具
 */
public class TerminalOperationTool {

    private final boolean enabled;
    private final Path workingDir;
    private final Duration timeout;
    private final int maxOutputChars;
    private final List<String> blockedCommands;

    public TerminalOperationTool(boolean enabled, Path workingDir, int timeoutSeconds, int maxOutputChars, List<String> blockedCommands) {
        this.enabled = enabled;
        this.workingDir = workingDir.toAbsolutePath().normalize();
        this.timeout = Duration.ofSeconds(timeoutSeconds);
        this.maxOutputChars = maxOutputChars;
        this.blockedCommands = blockedCommands.stream()
                .map(command -> command.toLowerCase(Locale.ROOT))
                .toList();
    }

    @Tool(description = "Execute a command in the terminal")
    public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal") String command) {
        if (!enabled) {
            return "Terminal tool is disabled by configuration.";
        }
        String normalizedCommand = command == null ? "" : command.trim().toLowerCase(Locale.ROOT);
        if (normalizedCommand.isBlank()) {
            return "Command is empty.";
        }
        if (blockedCommands.stream().anyMatch(normalizedCommand::contains)) {
            return "Command blocked by terminal safety policy.";
        }
        if (!Files.exists(workingDir)) {
            try {
                Files.createDirectories(workingDir);
            } catch (IOException e) {
                return "Error preparing working directory: " + e.getMessage();
            }
        }
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.directory(workingDir.toFile());
            builder.redirectErrorStream(true);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    if (output.length() >= maxOutputChars) {
                        output.append("Output truncated.");
                        break;
                    }
                }
            }
            boolean completed = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!completed) {
                process.destroyForcibly();
                return output.append("Command timed out after ").append(timeout.toSeconds()).append(" seconds.").toString();
            }
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}

