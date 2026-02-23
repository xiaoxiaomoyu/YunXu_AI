# YunXu AI

YunXu AI is an intelligent conversation application built on Spring Boot and Ollama, supporting streaming output and conversation history management.

## Project Overview

This project provides an AI chat interface that implements intelligent conversation functionality by integrating Ollama large language models. Developed using the Spring Boot framework, it supports streaming responses and session management.

## Technology Stack

- **Backend Framework**: Spring Boot
- **AI Model**: Ollama
- **Programming Language**: Java
- **Build Tool**: Maven

## Main Features

### 1. Intelligent Conversation
- Supports streaming output for real-time AI responses
- UTF-8 encoding support to prevent Chinese character garbling
- Multi-turn conversation support based on session ID

### 2. Conversation History Management
- Automatically saves conversation history
- Supports viewing session lists by type
- Retrieves complete conversation content for a specified session

### 3. CORS Configuration
- Cross-Origin Resource Sharing (CORS) support
- Allows frontend applications to make cross-origin API calls

## Project Structure

```
src/main/java/com/example/yunxu_ai/
├── YunXuAiApplication.java          # Main application class
├── config/
│   ├── CommonConfiguration.java      # General configuration (ChatMemory, ChatClient)
│   └── MvcConfiguration.java        # MVC configuration (CORS)
├── controller/
│   ├── ChatController.java          # Chat API controller
│   └── ChatHistoryController.java   # Conversation history controller
├── entity/vo/
│   └── MessageVO.java               # Message Value Object class
├── enums/
│   └── ChatType.java                # Chat type enum
├── repository/
│   ├── ChatHistoryRepository.java   # Chat history repository interface
│   └── InMemoryChatHistoryRepository.java  # In-memory implementation
└── service/
    └── ChatHistoryService.java      # Conversation history service
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

**Response Type**: `text/event-stream` (streaming response)

**Response Example**:
```
Hello! I am an AI assistant. How can I help you?
```

### Get Session List

**Endpoint**: `/ai/history/{type}`

**Method**: GET

**Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type      | String | Yes      | Session type |

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
- Ollama service (local or remote)

### Installation Steps

1. **Clone the Project**
```bash
git clone https://gitee.com/XiaoXiao_MoYu/YunXu_AI.git
cd YunXu_AI
```

2. **Configure Ollama**
Ensure the Ollama service is running and configure the correct model endpoint in `application.yaml`.

3. **Build the Project**
```bash
./mvnw clean package
```

4. **Run the Application**
```bash
java -jar target/yunxu_ai-0.0.1-SNAPSHOT.jar
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

# Ollama configuration
ollama:
  chat:
    model: llama2  # Model name to use
    timeout: 120s  # Request timeout
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
```

## License

This project is open-sourced under the [MIT License](LICENSE).