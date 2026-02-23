

# YunXu AI

YunXu AI 是一个基于 Spring Boot 和 Ollama 构建的智能对话应用，支持流式输出和对话历史管理。

## 项目简介

本项目提供了一个 AI 聊天接口，通过集成 Ollama 大语言模型，实现了智能对话功能。项目采用 Spring Boot 框架开发，支持流式响应和会话管理。

## 技术栈

- **后端框架**: Spring Boot
- **AI 模型**: Ollama / OpenAI
- **编程语言**: Java
- **构建工具**: Maven

## 主要功能

### 1. 智能对话
- 支持流式输出，实时获取 AI 响应
- UTF-8 编码支持，防止中文乱码
- 基于会话 ID 的多轮对话支持
- 支持普通对话和游戏角色对话两种模式

### 2. 对话历史管理
- 自动保存对话历史记录
- 支持按类型查看会话列表
- 获取指定会话的完整对话内容

### 3. CORS 配置
- 跨域资源共享支持
- 允许前端应用跨域调用接口

## 项目结构

```
src/main/java/com/example/yunxu_ai/
├── YunXuAiApplication.java          # 启动类
├── constants/
│   └── SystemConstants.java        # 系统常量定义
├── config/
│   ├── CommonConfiguration.java     # 通用配置（ChatMemory、ChatClient）
│   └── MvcConfiguration.java        # MVC配置（CORS跨域）
├── controller/
│   ├── ChatController.java          # 聊天接口控制器
│   ├── ChatHistoryController.java   # 对话历史控制器
│   └── GameController.java          # 游戏角色对话控制器
├── entity/vo/
│   └── MessageVO.java               # 消息VO类
├── enums/
│   └── ChatType.java                # 聊天类型枚举
├── repository/
│   ├── ChatHistoryRepository.java   # 聊天历史仓库接口
│   └── InMemoryChatHistoryRepository.java  # 内存实现
└── service/
    └── ChatHistoryService.java      # 聊天历史服务
```

## API 接口

### 聊天接口

**接口地址**: `/ai/chat`

**请求方式**: GET/POST

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prompt | String | 是 | 用户输入的问题 |
| chatId | String | 是 | 会话 ID，用于标识一次对话 |

**返回类型**: `text/event-stream` (流式响应)

### 游戏角色对话接口

**接口地址**: `/ai/game`

**请求方式**: GET/POST

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prompt | String | 是 | 用户输入的问题 |
| chatId | String | 是 | 会话 ID，用于标识一次对话 |

**返回类型**: `text/event-stream` (流式响应)

### 获取会话列表

**接口地址**: `/ai/history/{type}`

**请求方式**: GET

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 是 | 会话类型 |

**返回示例**:
```json
["chat_001", "chat_002", "chat_003"]
```

### 获取会话详情

**接口地址**: `/ai/history/{type}/{chatId}`

**请求方式**: GET

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 是 | 会话类型 |
| chatId | String | 是 | 会话 ID |

**返回示例**:
```json
[
  {
    "role": "user",
    "content": "你好"
  },
  {
    "role": "assistant",
    "content": "你好！有什么可以帮你的吗？"
  }
]
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Ollama 服务 (本地或远程)

### 安装步骤

1. **克隆项目**
```bash
git clone https://gitee.com/XiaoXiao_MoYu/YunXu_AI.git
cd YunXu_AI
```

2. **配置 Ollama**
确保 Ollama 服务已启动，并配置正确的模型地址（在 `application.yaml` 中配置）

3. **构建项目**
```bash
./mvnw clean package
```

4. **运行应用**
```bash
java -jar target/yunxu_ai-0.0.1-SNAPSHOT.jar
```

### 使用 Docker 运行

```bash
docker build -t yunxu-ai .
docker run -p 8080:8080 yunxu-ai
```

## 配置说明

在 `src/main/resources/application.yaml` 中进行配置：

```yaml
server:
  port: 8080

# Ollama 配置
ollama:
  chat:
    model: llama2  # 使用的模型名称
    timeout: 120s  # 请求超时时间
```

## 前端集成示例

```javascript
// 发起流式聊天请求
async function chat(prompt, chatId) {
  const response = await fetch(`/ai/chat?prompt=${encodeURIComponent(prompt)}&chatId=${chatId}`);
  const reader = response.body.getReader();
  const decoder = new TextDecoder();
  
  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    const chunk = decoder.decode(value);
    console.log(chunk); // 处理流式数据
  }
}
```

## 许可证

本项目基于 [MIT License](LICENSE) 开源。