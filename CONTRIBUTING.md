# 贡献指南

感谢你愿意一起维护 YunXu AI。这个项目目前包含 Spring Boot 后端和 Vue 3 前端，提交时请尽量保持变更小而清晰，方便回溯和 review。

## 分支与提交

建议从 `master` 拉出短分支开发：

```bash
git checkout master
git pull
git checkout -b feat/your-change
```

提交信息使用 Conventional Commits 风格：

```text
<type>(<scope>): <subject>
```

常用类型：

| Type | 用途 |
| --- | --- |
| `feat` | 新功能 |
| `fix` | 缺陷修复 |
| `refactor` | 不改变外部行为的重构 |
| `docs` | 文档更新 |
| `test` | 测试相关 |
| `chore` | 构建、依赖、配置等维护工作 |
| `ci` | CI/CD 配置 |

示例：

```text
feat(chat): 添加多模态聊天入口
fix(test): 排除重复 JSON 测试依赖
refactor(ai): 迁移至 DashScope 配置
docs(readme): 更新本地启动说明
ci(github): 添加 Maven 测试工作流
```

## 本地验证

后端测试：

```bash
./mvnw test
```

前端构建：

```bash
cd frontend
npm install
npm run build
```

提交前请确认：

- 没有提交 `target/`、`node_modules/`、`dist/` 等生成目录。
- 没有提交真实的 API Key、数据库密码或本机私有配置。
- README、接口说明和实际代码保持一致。
