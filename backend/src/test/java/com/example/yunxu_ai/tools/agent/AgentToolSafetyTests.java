package com.example.yunxu_ai.tools.agent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AgentToolSafetyTests {

    @TempDir
    Path tempDir;

    @Test
    void fileToolRejectsPathTraversal() {
        FileOperationTool tool = new FileOperationTool(tempDir);

        String result = tool.writeFile("../outside.txt", "nope");

        assertThat(result).contains("Error writing to file");
        assertThat(Files.exists(tempDir.resolveSibling("outside.txt"))).isFalse();
    }

    @Test
    void terminalToolIsDisabledByDefault() {
        TerminalOperationTool tool = new TerminalOperationTool(false, tempDir, 1, 1000, List.of("del"));

        String result = tool.executeTerminalCommand("echo hello");

        assertThat(result).contains("disabled");
    }

    @Test
    void terminalToolBlocksDangerousCommandsWhenEnabled() {
        TerminalOperationTool tool = new TerminalOperationTool(true, tempDir, 1, 1000, List.of("del", "git reset"));

        String result = tool.executeTerminalCommand("git reset --hard");

        assertThat(result).contains("blocked");
    }
}
