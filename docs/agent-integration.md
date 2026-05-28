# 云絮 AI 智能体整合说明

本项目已整合恋爱大师、云絮超级智能体、RAG、工具调用和 MCP 预留能力。默认配置优先保证原有应用稳定启动，可选能力均通过环境变量或配置开关启用。

## 默认启用

- `/ai/love-app/chat/sync`：恋爱大师同步对话。
- `/ai/love-app/chat/sse`：恋爱大师 SSE 流式对话，结束标记为 `[DONE]`。
- `/ai/manus/chat`：云絮超级智能体 SSE 对话。
- 本地 Markdown RAG：读取 `backend/src/main/resources/document/*.md`，使用独立 `loveAppVectorStore`。
- 工具系统：课程工具、文件读写、搜索、网页抓取、资源下载、PDF 生成和终止工具。

## 默认预留

- 终端工具默认关闭：设置 `YUNXU_AGENT_TERMINAL_ENABLED=true` 后启用，并受工作目录、超时、输出长度和危险命令拦截限制。
- MCP 客户端默认关闭：设置 `YUNXU_MCP_CLIENT_ENABLED=true` 后读取 `classpath:mcp-servers.json`。
- 图片搜索 MCP 服务位于 `mcp/yunxu-image-search-mcp-server`，需要先构建 jar，并设置 `PEXELS_API_KEY`。
- PGVector 默认关闭：设置 `YUNXU_PGVECTOR_ENABLED=true` 并配置 `YUNXU_PGVECTOR_URL`、`YUNXU_PGVECTOR_USERNAME`、`YUNXU_PGVECTOR_PASSWORD`。
- 文件持久化记忆默认关闭：设置 `YUNXU_LOVE_APP_FILE_MEMORY_ENABLED=true` 后使用 `tmp/chat-memory`。
- 结构化输出依赖已预留，`LoveApp#doChatWithReport` 可直接生成报告对象。
- Knife4j 地址：`/doc.html`；OpenAPI 地址：`/v3/api-docs`。

## 环境变量

- `DASHSCOPE_API_KEY`：主模型和 Embedding 所需。
- `SEARCH_API_API_KEY`：网页搜索工具所需。
- `PEXELS_API_KEY`：图片搜索 MCP 服务所需。
- `AMAP_MAPS_API_KEY`：高德 MCP 服务所需。

## 构建命令

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21'
cd backend
mvn -q -DskipTests compile
mvn -q test
```

```powershell
mvn -f mcp/yunxu-image-search-mcp-server/pom.xml -q -DskipTests compile
```

```powershell
cd frontend
npm.cmd run build
```
