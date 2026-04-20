# YunXu AI

YunXu AI 是一个基于 Spring Boot、Spring AI Alibaba DashScope 和 Vue 3 的智能对话应用。项目包含后端 API 与前端页面，当前支持通用 AI 对话、带附件的多模态对话、哄哄模拟器、课程咨询客服和会话历史管理。

## 项目亮点

- **流式对话**：通过 Spring AI `ChatClient` 返回实时响应。
- **DashScope 接入**：默认使用 `qwen-flash`，通过 `DASHSCOPE_API_KEY` 注入密钥。
- **多场景会话**：普通聊天、游戏角色扮演、课程客服三类入口独立配置。
- **工具调用**：客服场景可查询课程、校区，并创建课程预约记录。
- **前后端一体**：后端位于仓库根目录，Vue 3 前端位于 `frontend/`。
- **CI 校验**：GitHub Actions 会在 push 和 PR 时运行后端测试与前端构建。

## 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Java 17, Spring Boot 3.5.6, Spring AI 1.1.2 |
| AI 服务 | Spring AI Alibaba DashScope 1.1.2.0 |
| 数据存储 | MySQL, Redis |
| ORM | MyBatis-Plus |
| 前端 | Vue 3, Vite 6, TypeScript, Pinia, Naive UI |
| 构建 | Maven Wrapper, npm |

## 目录结构

```text
.
├── .github/workflows/ci.yml          # GitHub Actions CI
├── docs/yunxu_ai_database.sql        # MySQL 初始化脚本
├── frontend/                         # Vue 3 前端
│   ├── src/
│   ├── public/
│   └── package.json
├── src/main/java/com/example/yunxu_ai/
│   ├── config/                       # ChatClient、CORS 等配置
│   ├── controller/                   # AI 对话、客服、游戏、历史接口
│   ├── entity/                       # PO、VO、查询对象
│   ├── mapper/                       # MyBatis Mapper
│   ├── repository/                   # 会话历史仓储
│   ├── service/                      # 业务服务
│   ├── tools/                        # AI 工具调用
│   └── util/                         # 工具类
├── src/main/resources/
│   ├── application.yaml              # 应用配置
│   └── mapper/                       # MyBatis XML
├── CONTRIBUTING.md                   # 提交规范
├── pom.xml
└── README.md
```

## 快速开始

### 环境要求

- JDK 17+
- Node.js 22+
- MySQL 8+
- Redis 6+
- DashScope API Key

### 后端启动

1. 克隆仓库并进入项目：

```bash
git clone https://github.com/xiaoxiaomoyu/YunXu_AI.git
cd YunXu_AI
```

1. 准备 MySQL 数据库并导入初始化脚本，然后确认 Redis 已启动。

```bash
mysql -u root -p < docs/yunxu_ai_database.sql
```

1. 配置环境变量：

```bash
export DASHSCOPE_API_KEY=your_dashscope_api_key
```

Windows PowerShell：

```powershell
$env:DASHSCOPE_API_KEY="your_dashscope_api_key"
```

1. 按需修改 `src/main/resources/application.yaml` 中的 MySQL 与 Redis 连接信息。

1. 启动后端：

```bash
./mvnw spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端默认运行在 Vite 输出的本地地址，接口默认请求 `http://localhost:8080`。

## 常用命令

后端测试：

```bash
./mvnw test
```

前端构建：

```bash
cd frontend
npm run build
```

## API 概览

### 通用聊天

```http
POST /ai/chat
GET  /ai/chat
```

参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `prompt` | String | 是 | 用户输入 |
| `chatId` | String | 是 | 会话 ID |
| `files` | MultipartFile[] | 否 | 图片、音频、视频等附件 |

返回：`text/html;charset=utf-8` 流式文本。

### 哄哄模拟器

```http
GET /ai/game
POST /ai/game
```

参数：`prompt`、`chatId`。返回流式文本。

### 智能客服

```http
GET /ai/service
POST /ai/service
```

参数：`prompt`、`chatId`。客服场景可调用课程、校区与预约工具。

### 会话历史

```http
GET /ai/history/{type}
GET /ai/history/{type}/{chatId}
```

`type` 可使用 `chat`、`service` 等业务类型。

## 配置说明

核心配置位于 `src/main/resources/application.yaml`：

- `spring.ai.dashscope.api-key`：从 `DASHSCOPE_API_KEY` 环境变量读取。
- `spring.ai.dashscope.chat.options.model`：默认 `qwen-flash`。
- `spring.datasource`：MySQL 连接。
- `spring.data.redis`：Redis 连接。
- `server.port`：默认 `8080`。

## 提交规范

项目使用 Conventional Commits 风格，详见 [CONTRIBUTING.md](CONTRIBUTING.md)。

示例：

```text
feat(chat): 添加多模态聊天入口
fix(test): 排除重复 JSON 测试依赖
refactor(ai): 迁移至 DashScope 配置
docs(readme): 更新本地启动说明
```

## 当前状态

PDF 智能问答相关后端代码已移除，README 与依赖也已同步清理。若后续要恢复文档问答，建议重新设计为独立模块，并同时补齐前端入口、后端接口、向量索引和测试。
