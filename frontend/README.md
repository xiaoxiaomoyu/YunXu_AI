# 云絮 AI 前端

`frontend/` 是云絮 AI 的 Vue 3 + Vite 前端项目，负责首页入口、通用聊天、课程客服、哄哄模拟器、恋爱大师和云絮超级智能体页面。

## 常用命令

```powershell
npm.cmd install
npm.cmd run dev
npm.cmd run build
```

## 主要路由

- `/`：应用首页
- `/ai-chat`：通用 AI 聊天
- `/customer-service`：课程智能客服
- `/game`：哄哄模拟器
- `/love-master`：恋爱大师
- `/super-agent`：云絮超级智能体

## 接口封装

- 原有业务接口保留在 `src/services/api.js`。
- 智能体 SSE 接口位于 `src/services/agentApi.ts`。
- 默认后端地址为 `http://localhost:8080`。
