# YunXu AI

YunXu AI is an intelligent conversation application built on Spring Boot and Spring AI, supporting streaming output, multi-modal dialogue, PDF intelligent Q&A, and conversation history management.

## Project Overview

This project provides a feature-rich AI chat interface that integrates Ollama and OpenAI (Alibaba Cloud Qwen) large language models to implement various intelligent conversation functions. Developed using the Spring Boot framework, it supports advanced features such as streaming responses, session management, and vector retrieval.

## Technology Stack

- **Backend Framework**: Spring Boot 3.5.6
- **AI Framework**: Spring AI 1.1.2
- **AI Models**: Ollama / OpenAI (Alibaba Cloud Qwen)
- **Database**: MySQL + Redis (Vector Store)
- **ORM Framework**: MyBatis Plus
- **Programming Language**: Java 17
- **Build Tool**: Maven

## Main Features

### 1. Intelligent Conversation
- Supports streaming output for real-time AI responses
- UTF-8 encoding support to prevent Chinese character garbling
- Multi-turn conversation support based on session ID
- **Multi-modal Dialogue**: Supports uploading images/files for multi-modal conversations

### 2. PDF Intelligent Q&A
- Upload PDF files and converse with document content
- Intelligent Q&A based on vector retrieval
- Supports file upload and download

### 3. Intelligent Customer Service System
- Intelligent customer service based on course database
- Supports course query and school query
- Supports course reservation generation
- Function Calling capability

### 4. Game Character Dialogue
- Coaxing simulator game mode
- Immersive role-playing dialogue experience

### 5. Conversation History Management
- Automatically saves conversation history
- Supports viewing session lists by type
- Retrieves complete conversation content for a specified session

### 6. CORS Configuration
- Cross-Origin Resource Sharing (CORS) support
- Allows frontend applications to make cross-origin API calls

## Project Structure

```
src/main/java/com/example/yunxu_ai/
├── YunXuAiApplication.java          # Main application class
├── config/
│   ├── CommonConfiguration.java     # General configuration (ChatMemory, ChatClient, VectorStore)
│   └── MvcConfiguration.java        # MVC configuration (CORS)
├── constants/
│   └── SystemConstants.java         # System constants definition
├── controller/
│   ├── ChatController.java          # Chat API controller
│   ├── ChatHistoryController.java   # Conversation history controller
│   ├── CustomerServiceController.java # Intelligent customer service controller
│   ├── GameController.java          # Game character dialogue controller
│   └── PdfController.java           # PDF intelligent Q&A controller
├── entity/
│   ├── po/
│   │   ├── Course.java              # Course entity
│   │   ├── CourseReservation.java   # Course reservation entity
│   │   └── School.java              # School entity
│   ├── query/
│   │   └── CourseQuery.java         # Course query condition
│   └── vo/
│       ├── MessageVO.java           # Message Value Object class
│       └── Result.java              # Unified response result
├── enums/
│   └── ChatType.java                # Chat type enum
├── mapper/
│   ├── CourseMapper.java            # Course Mapper
│   ├── CourseReservationMapper.java # Course reservation Mapper
│   └── SchoolMapper.java            # School Mapper
├── repository/
│   ├── ChatHistoryRepository.java   # Chat history repository interface
│   ├── FileRepository.java          # File repository interface
│   ├── InMemoryChatHistoryRepository.java  # In-memory implementation
│   └── LocalPdfFileRepository.java  # Local PDF file repository
├── service/
│   ├── impl/
│   │   ├── CourseReservationServiceImpl.java
│   │   ├── CourseServiceImpl.java
│   │   └── SchoolServiceImpl.java
│   ├── ChatHistoryService.java      # Chat history service
│   ├── ICourseReservationService.java
│   ├── ICourseService.java
│   └── ISchoolService.java
├── tools/
│   └── CourseTools.java             # AI tools (course query, reservation)
└── util/
    └── VectorDistanceUtils.java     # Vector distance utility class
```

## API Endpoints

### Chat Endpoint

**Endpoint**: `/ai/chat`

**Method**: GET/POST

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| prompt    | String | Yes      | User input question |
| chatId    | String | Yes      | Session ID identifying the conversation |
| files     | MultipartFile[] | No | Uploaded files (supports multi-modal dialogue) |

**Response Type**: `text/html;charset=UTF-8` (streaming response)

### Game Character Dialogue Endpoint

**Endpoint**: `/ai/game`

**Method**: GET/POST

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| prompt    | String | Yes      | User input question |
| chatId    | String | Yes      | Session ID identifying the conversation |

**Response Type**: `text/html;charset=UTF-8` (streaming response)

### Intelligent Customer Service Endpoint

**Endpoint**: `/ai/service`

**Method**: GET/POST

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| prompt    | String | Yes      | User input question |
| chatId    | String | Yes      | Session ID identifying the conversation |

**Response Type**: `text/html;charset=UTF-8` (streaming response)

**Features**: Supports course query, school query, course reservation, etc.

### PDF Intelligent Q&A Endpoints

#### Upload PDF File

**Endpoint**: `/ai/pdf/upload/{chatId}`

**Method**: POST (multipart/form-data)

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| chatId    | String | Yes      | Session ID (path parameter) |
| file      | MultipartFile | Yes  | PDF file |

**Response Example**:
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

#### Download PDF File

**Endpoint**: `/ai/pdf/file/{chatId}`

**Method**: GET

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| chatId    | String | Yes      | Session ID (path parameter) |

**Response Type**: `application/octet-stream`

#### PDF Chat

**Endpoint**: `/ai/pdf/chat`

**Method**: GET/POST

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| prompt    | String | Yes      | User input question |
| chatId    | String | Yes      | Session ID |

**Response Type**: `text/html;charset=UTF-8` (streaming response)

### Get Session List

**Endpoint**: `/ai/history/{type}`

**Method**: GET

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type      | String | Yes      | Session type (chat/game/service/pdf) |

**Response Example**:
```json
["chat_001", "chat_002", "chat_003"]
```

### Get Session Details

**Endpoint**: `/ai/history/{type}/{chatId}`

**Method**: GET

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type      | String | Yes      | Session type |
| chatId    | String | Yes      | Session ID |

**Response Example**:
```json
[
  {
    "role": "user",
    "content": "Hello"
  },
  {
    "role": "assistant",
    "content": "Hello! How can I help you?"
  }
]
```

## Quick Start

### Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Ollama service (local or remote)

### Installation Steps

1. **Clone the Project**
```bash
git clone https://gitee.com/XiaoXiao_MoYu/YunXu_AI.git
cd YunXu_AI
```

2. **Configure Database**
Create MySQL database and import related table structures

3. **Configure Environment Variables**
Set `OPENAI_API_KEY` environment variable (Alibaba Cloud Qwen API Key)

4. **Modify Configuration File**
Configure database connection, Redis connection, Ollama address, etc. in `src/main/resources/application.yaml`

5. **Build the Project**
```bash
./mvnw clean package
```

6. **Run the Application**
```bash
java -jar target/YunXu_AI-0.0.1-SNAPSHOT.jar
```

### Run with Docker

```bash
docker build -t yunxu-ai .
docker run -p 8080:8080 yunxu-ai
```

## Configuration

Configure settings in `src/main/resources/application.yaml`:

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

## Frontend Integration Example

```javascript
// Initiate streaming chat request
async function chat(prompt, chatId) {
  const response = await fetch(`/ai/chat?prompt=${encodeURIComponent(prompt)}&chatId=${chatId}`);
  const reader = response.body.getReader();
  const decoder = new TextDecoder();
  
  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    const chunk = decoder.decode(value);
    console.log(chunk); // Process streaming data
  }
}

// Upload PDF and chat
async function uploadAndChat(file, chatId, prompt) {
  // 1. Upload file
  const formData = new FormData();
  formData.append('file', file);
  await fetch(`/ai/pdf/upload/${chatId}`, {
    method: 'POST',
    body: formData
  });
  
  // 2. Initiate chat
  const response = await fetch(`/ai/pdf/chat?prompt=${encodeURIComponent(prompt)}&chatId=${chatId}`);
  // Process streaming response...
}
```

## License

This project is open-sourced under the [MIT License](LICENSE).
