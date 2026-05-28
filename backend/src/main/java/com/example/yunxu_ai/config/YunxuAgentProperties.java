package com.example.yunxu_ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "yunxu.agent")
public class YunxuAgentProperties {

    private String workspaceDir = "tmp/agent-workspace";
    private Terminal terminal = new Terminal();
    private LoveApp loveApp = new LoveApp();
    private Rag rag = new Rag();

    public String getWorkspaceDir() {
        return workspaceDir;
    }

    public void setWorkspaceDir(String workspaceDir) {
        this.workspaceDir = workspaceDir;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public LoveApp getLoveApp() {
        return loveApp;
    }

    public void setLoveApp(LoveApp loveApp) {
        this.loveApp = loveApp;
    }

    public Rag getRag() {
        return rag;
    }

    public void setRag(Rag rag) {
        this.rag = rag;
    }

    public static class Terminal {
        private boolean enabled;
        private int timeoutSeconds = 10;
        private int maxOutputChars = 4000;
        private String allowedWorkingDir = "tmp/agent-workspace";
        private List<String> blockedCommands = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        public int getMaxOutputChars() {
            return maxOutputChars;
        }

        public void setMaxOutputChars(int maxOutputChars) {
            this.maxOutputChars = maxOutputChars;
        }

        public String getAllowedWorkingDir() {
            return allowedWorkingDir;
        }

        public void setAllowedWorkingDir(String allowedWorkingDir) {
            this.allowedWorkingDir = allowedWorkingDir;
        }

        public List<String> getBlockedCommands() {
            return blockedCommands;
        }

        public void setBlockedCommands(List<String> blockedCommands) {
            this.blockedCommands = blockedCommands;
        }
    }

    public static class LoveApp {
        private boolean fileMemoryEnabled;
        private String fileMemoryDir = "tmp/chat-memory";

        public boolean isFileMemoryEnabled() {
            return fileMemoryEnabled;
        }

        public void setFileMemoryEnabled(boolean fileMemoryEnabled) {
            this.fileMemoryEnabled = fileMemoryEnabled;
        }

        public String getFileMemoryDir() {
            return fileMemoryDir;
        }

        public void setFileMemoryDir(String fileMemoryDir) {
            this.fileMemoryDir = fileMemoryDir;
        }
    }

    public static class Rag {
        private Pgvector pgvector = new Pgvector();

        public Pgvector getPgvector() {
            return pgvector;
        }

        public void setPgvector(Pgvector pgvector) {
            this.pgvector = pgvector;
        }
    }

    public static class Pgvector {
        private boolean enabled;
        private String url = "jdbc:postgresql://localhost:5432/yunxu_ai";
        private String username = "postgres";
        private String password = "postgres";
        private String schemaName = "public";
        private String tableName = "vector_store";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }
}
