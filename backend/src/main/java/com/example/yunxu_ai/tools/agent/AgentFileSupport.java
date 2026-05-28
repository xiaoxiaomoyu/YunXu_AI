package com.example.yunxu_ai.tools.agent;

import java.nio.file.Path;

final class AgentFileSupport {

    private AgentFileSupport() {
    }

    static Path resolveSafePath(Path rootDir, String childPath) {
        Path root = rootDir.toAbsolutePath().normalize();
        Path target = root.resolve(childPath).normalize();
        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("Path is outside the agent workspace");
        }
        return target;
    }
}
