# YunXu AI

YunXu AI 是一个基于 Spring Boot 和 Spring AI 构建的智能对话应用，支持流式输出、多模态对话、PDF 智能问答和对话历史管理。

## 项目简介

本项目提供了一个功能丰富的 AI 聊天接口，通过集成 Ollama 和 OpenAI（阿里云通义千问）大语言模型，实现了多种智能对话功能。项目采用 Spring Boot 框架开发，支持流式响应、会话管理、向量检索等高级特性。

## 技术栈

- **后端框架**: Spring Boot 3.5.6
- **AI 框架**: Spring AI 1.1.2
- **AI 模型**: Ollama / OpenAI (阿里云通义千问)
- **数据库**: MySQL + Redis (向量存储)
- **ORM 框架**: MyBatis Plus
- **编程语言**: Java 17
- **构建工具**: Maven

## 主要功能

### 1. 智能对话
- 支持流式输出，实时获取 AI 响应
- UTF-8 编码支持，防止中文乱码
- 基于会话 ID 的多轮对话支持
- **多模态对话**: 支持上传图片/文件进行多模态对话

### 2. PDF 智能问答
- 上传 PDF 文件后可与文档内容进行对话
- 基于向量检索的智能问答
- 支持文件上传和下载

### 3. 智能客服系统
- 基于课程数据库的智能客服
- 支持课程查询、校区查询
- 支持课程预约单生成
- Function Calling 能力

### 4. 游戏角色对话
- 哄哄模拟器游戏模式
- 沉浸式角色扮演对话体验

### 5. 对话历史管理
- 自动保存对话历史记录
- 支持按类型查看会话列表
- 获取指定会话的完整对话内容

### 6. CORS 配置
- 跨域资源共享支持
- 允许前端应用跨域调用接口

## 项目结构

```
src/main/java/com/example/yunxu_ai/
├── YunXuAiApplication.java          # 启动类
├── config/
│   ├── CommonConfiguration.java     # 通用配置（ChatMemory、ChatClient、VectorStore）
│   └── MvcConfiguration.java        # MVC配置（CORS跨域）
├── constants/
│   └── SystemConstants.java         # 系统常量定义
├── controller/
│   ├── ChatController.java          # 聊天接口控制器
│   ├── ChatHistoryController.java   # 对话历史控制器
│   ├── CustomerServiceController.java # 智能客服控制器
│   ├── GameController.java          # 游戏角色对话控制器
│   └── PdfController.java           # PDF智能问答控制器
├── entity/
│   ├── po/
│   │   ├── Course.java              # 课程实体
│   │   ├── CourseReservation.java   # 课程预约实体
│   │   └── School.java              # 校区实体
│   ├── query/
│   │   └── CourseQuery.java         # 课程查询条件
│   └── vo/
│       ├── MessageVO.java           # 消息VO类
│       └── Result.java              # 统一响应结果
├── enums/
│   └── ChatType.java                # 聊天类型枚举
├── mapper/
│   ├── CourseMapper.java            # 课程Mapper
│   ├── CourseReservationMapper.java # 课程预约Mapper
│   └── SchoolMapper.java            # 校区Mapper
├── repository/
│   ├── ChatHistoryRepository.java   # 聊天历史仓库接口
│   ├── FileRepository.java          # 文件仓库接口
│   ├── InMemoryChatHistoryRepository.java  # 内存实现
│   └── LocalPdfFileRepository.java  # 本地PDF文件仓库
├── service/
│   ├── impl/
│   │   ├── CourseReservationServiceImpl.java
│   │   ├── CourseServiceImpl.java
│   │   └── SchoolServiceImpl.java
│   ├── ChatHistoryService.java      # 聊天历史服务
│   ├── ICourseReservationService.java
│   ├── ICourseService.java
│   └── ISchoolService.java
├── tools/
│   └── CourseTools.java             # AI工具类（课程查询、预约）
└── util/
    └── VectorDistanceUtils.java     # 向量距离工具类
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
| files | MultipartFile[] | 否 | 上传的文件（支持多模态对话） |

**返回类型**: `text/html;charset=UTF-8` (流式响应)

### 游戏角色对话接口

**接口地址**: `/ai/game`

**请求方式**: GET/POST

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prompt | String | 是 | 用户输入的问题 |
| chatId | String | 是 | 会话 ID，用于标识一次对话 |

**返回类型**: `text/html;charset=UTF-8` (流式响应)

### 智能客服接口

**接口地址**: `/ai/service`

**请求方式**: GET/POST

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prompt | String | 是 | 用户输入的问题 |
| chatId | String | 是 | 会话 ID，用于标识一次对话 |

**返回类型**: `text/html;charset=UTF-8` (流式响应)

**功能说明**: 支持课程查询、校区查询、课程预约等功能

### PDF 智能问答接口

#### 上传 PDF 文件

**接口地址**: `/ai/pdf/upload/{chatId}`

**请求方式**: POST (multipart/form-data)

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| chatId | String | 是 | 会话 ID (路径参数) |
| file | MultipartFile | 是 | PDF 文件 |

**返回示例**:
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

#### 下载 PDF 文件

**接口地址**: `/ai/pdf/file/{chatId}`

**请求方式**: GET

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| chatId | String | 是 | 会话 ID (路径参数) |

**返回类型**: `application/octet-stream`

#### PDF 对话

**接口地址**: `/ai/pdf/chat`

**请求方式**: GET/POST

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prompt | String | 是 | 用户输入的问题 |
| chatId | String | 是 | 会话 ID |

**返回类型**: `text/html;charset=UTF-8` (流式响应)

### 获取会话列表

**接口地址**: `/ai/history/{type}`

**请求方式**: GET

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 是 | 会话类型 (chat/game/service/pdf) |

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
- MySQL 8.0+
- Redis 6.0+
- Ollama 服务 (本地或远程)

### 安装步骤

1. **克隆项目**
```bash
git clone https://gitee.com/XiaoXiao_MoYu/YunXu_AI.git
cd YunXu_AI
```

2. **配置数据库**
创建 MySQL 数据库并导入相关表结构

3. **配置环境变量**
设置 `OPENAI_API_KEY` 环境变量（阿里云通义千问 API Key）

4. **修改配置文件**
在 `src/main/resources/application.yaml` 中配置数据库连接、Redis 连接、Ollama 地址等

5. **构建项目**
```bash
./mvnw clean package
```

6. **运行应用**
```bash
java -jar target/YunXu_AI-0.0.1-SNAPSHOT.jar
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

spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: deepseek-r1:7b
        options:
          temperature: 0.7
          num-predict: 2000
    openai:
      base-url: https://dashscope.aliyuncs.com/compatible-mode
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: qwen-max-latest
      embedding:
        options:
          model: text-embedding-v3
          dimensions: 1024
    vectorstore:
      redis:
        index-name: spring_ai_index
        initialize-schema: true
        prefix: "doc:"
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 30MB
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

// 上传 PDF 并对话
async function uploadAndChat(file, chatId, prompt) {
  // 1. 上传文件
  const formData = new FormData();
  formData.append('file', file);
  await fetch(`/ai/pdf/upload/${chatId}`, {
    method: 'POST',
    body: formData
  });
  
  // 2. 发起对话
  const response = await fetch(`/ai/pdf/chat?prompt=${encodeURIComponent(prompt)}&chatId=${chatId}`);
  // 处理流式响应...
}
```

## 许可证

本项目基于 [MIT License](LICENSE) 开源。
