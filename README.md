# 云絮 AI

云絮 AI 是一个基于 Spring Boot、Spring AI Alibaba DashScope 和 Vue 3 的智能对话项目。当前仓库以 `D:\JAVA\YunXu_AI` 为根目录管理，后端、前端、MCP 服务和文档分别放在独立目录中，便于后续维护。

## 功能概览

- 通用 AI 聊天：支持文本流式响应和多模态附件入口。
- 课程智能客服：保留课程、校区、预约等工具调用能力。
- 哄哄模拟器：保留原有游戏对话场景。
- 恋爱大师：提供同步和 SSE 两种对话接口，并接入本地 Markdown RAG。
- 云絮超级智能体：整合 Manus 风格工具调用、受限文件工作区、搜索、抓取、PDF、终止工具等能力。
- 可选扩展：MCP 图片搜索、PGVector、文件记忆、结构化输出、Knife4j 文档和受限终端工具均通过配置开关控制。

## 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Java 21, Spring Boot 3.5.6, Spring AI 1.1.2 |
| AI 服务 | Spring AI Alibaba DashScope 1.1.2.0 |
| 数据存储 | MySQL, Redis, 可选 PGVector |
| ORM | MyBatis-Plus |
| 前端 | Vue 3, Vite 6, TypeScript, Pinia, Naive UI |
| MCP | Spring AI MCP Server |

## 目录结构

```text
.
├── backend/                         # Spring Boot 后端
│   ├── src/main/java/com/example/yunxu_ai/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── mvnw.cmd
├── frontend/                        # Vue 3 前端
│   ├── src/
│   ├── public/
│   └── package.json
├── mcp/
│   └── yunxu-image-search-mcp-server/ # 图片搜索 MCP 服务
├── docs/
│   ├── agent-integration.md
│   └── yunxu_ai_database.sql
├── .github/workflows/ci.yml
├── CONTRIBUTING.md
└── README.md
```

## 快速开始

### 环境要求

- JDK 21
- Node.js 22+
- MySQL 8+
- Redis 6+
- DashScope API Key

### 后端启动

```powershell
cd backend
$env:JAVA_HOME='C:\Program Files\Java\jdk-21'
$env:DASHSCOPE_API_KEY='your_dashscope_api_key'
.\mvnw.cmd spring-boot:run
```

后端默认运行在 `http://localhost:8080`。数据库初始化脚本位于 `docs/yunxu_ai_database.sql`，核心配置位于 `backend/src/main/resources/application.yaml`。

### 前端启动

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

前端默认连接 `http://localhost:8080`，接口封装位于 `frontend/src/services/`。

### MCP 图片搜索服务

```powershell
mvn -f mcp/yunxu-image-search-mcp-server/pom.xml -q -DskipTests compile
```

主应用默认不连接 MCP。需要启用时，设置 `YUNXU_MCP_CLIENT_ENABLED=true`，并为图片搜索服务设置 `PEXELS_API_KEY`。

## 常用命令

```powershell
cd backend
mvn -q -DskipTests compile
mvn -q test
```

```powershell
cd frontend
npm.cmd run build
```

## 接口概览

```http
GET  /ai/chat
POST /ai/chat
GET  /ai/service
POST /ai/service
GET  /ai/game
POST /ai/game
GET  /ai/history/{type}
GET  /ai/history/{type}/{chatId}
GET  /ai/love-app/chat/sync
GET  /ai/love-app/chat/sse
GET  /ai/manus/chat
```

SSE 智能体接口使用 `[DONE]` 作为结束标记，方便前端可靠收尾。

## 文档

- 智能体整合说明：[docs/agent-integration.md](docs/agent-integration.md)
- 提交规范：[CONTRIBUTING.md](CONTRIBUTING.md)
