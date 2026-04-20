# YunXu AI

YunXu AI is an intelligent chat application built with Spring Boot, Spring AI Alibaba DashScope, and Vue 3. The repository now contains both the backend API and the frontend app.

## Features

- Streaming AI chat powered by Spring AI `ChatClient`.
- DashScope integration with the default `qwen-flash` model.
- Multiple chat scenarios: general chat, comfort simulator, course customer service, and chat history.
- Tool calling for course lookup, campus lookup, and course reservation.
- Vue 3 frontend under `frontend/`.
- GitHub Actions CI for backend tests and frontend builds.

## Tech Stack

| Area | Stack |
| --- | --- |
| Backend | Java 17, Spring Boot 3.5.6, Spring AI 1.1.2 |
| AI Provider | Spring AI Alibaba DashScope 1.1.2.0 |
| Storage | MySQL, Redis |
| ORM | MyBatis-Plus |
| Frontend | Vue 3, Vite 6, TypeScript, Pinia, Naive UI |
| Build | Maven Wrapper, npm |

## Quick Start

### Backend

```bash
git clone https://github.com/xiaoxiaomoyu/YunXu_AI.git
cd YunXu_AI
export DASHSCOPE_API_KEY=your_dashscope_api_key
./mvnw spring-boot:run
```

Update `src/main/resources/application.yaml` for your MySQL and Redis settings when needed.
You can initialize the sample database with:

```bash
mysql -u root -p < docs/yunxu_ai_database.sql
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend calls the backend at `http://localhost:8080` by default.

## Validation

Backend:

```bash
./mvnw test
```

Frontend:

```bash
cd frontend
npm run build
```

## Commit Convention

Use Conventional Commits. See [CONTRIBUTING.md](CONTRIBUTING.md).

Examples:

```text
feat(chat): add multimodal chat entry
fix(test): exclude duplicated JSON test dependency
docs(readme): update local setup guide
```

## Note

The former PDF Q&A backend module has been removed. If document Q&A is needed again, rebuild it as a dedicated module with matching frontend routes, backend APIs, vector indexing, and tests.
