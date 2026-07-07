# AGENTS.md

本文件为在本仓库中工作的 AI 编程代理提供协作规范。
同时也是 **treasure-platform**（集团司库管理系统后端）的项目技术说明。

---

## 项目概述

| 项目 | 说明 |
|------|------|
| 项目名称 | 集团司库管理系统 |
| 后端仓库 | treasure-platform（本仓库） |
| 前端仓库 | treasure-ui（独立仓库，在 treausre-ui/ 目录） |
| 策略路线 | **前端优先** — Phase 1 前端骨架已完成，Phase 2 开始后端骨架 |
| 建设阶段 | 4 个 Phase，详见下方「落地路线图」 |

### 后端

| 项 | 取值 |
|----|------|
| 语言 | Java 17 (Microsoft OpenJDK) |
| 框架 | Spring Boot 3.2.5 |
| 构建 | Maven 3.9.9（多模块） |
| 数据层 | MyBatis 3.0.3 + Druid 1.2.22 + PageHelper 2.1.0 |
| 数据库 | Oracle 12c（远程测试环境） |
| 未来迁移 | 达梦 DM8（已预留 databaseIdProvider 方言适配架构） |
| API 文档 | SpringDoc OpenAPI 2.5.0 |
| 认证 | Spring Security + JWT (jjwt 0.12.5) |

### 前端

| 项 | 取值 |
|----|------|
| 技术栈 | Vue 3 + TypeScript + Vite |
| UI 组件库 | Element Plus |
| 状态管理 | Pinia |
| 路由 | Vue Router |
| HTTP | Axios |
| 图标 | @iconify/vue |
| 布局参考 | 用友 NC Cloud 资金管理（布局理念）+ vue-vben-admin（技术方案） |
| Mock | mockjs + vite-plugin-mock |
| 本地运行 | Vite dev server（proxy 代理后端 API） |

### 本地环境

| 工具 | 状态 | 版本 |
|------|------|------|
| Java 17 | 已安装 | Microsoft OpenJDK 17.0.19 LTS |
| Maven | 已安装（.maven/） | 3.9.9 |
| Node.js | 已安装 | v26.4.0 |
| Docker | 待安装 | Phase 4 后再考虑 |
| Oracle | 远程测试环境 | 12c，通过 JDBC 直连 |

---

## 落地路线图（4 Phase）

```
Phase 1  (Done)     Phase 2  (In Progress)  Phase 3               Phase 4
┌────────────────┐   ┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│ 前端骨架        │ → │ 后端骨架        │ → │ 前后端联调      │ → │ 示例业务模块    │
│                │   │                │   │                │   │                │
│ Vue 3 项目创建  │   │ Maven 多模块    │   │ Mock 切真实 API │   │ 用户管理 CRUD  │
│ 登录页设计       │   │ 8 模块 POM 结构 │   │ 登录流程全链路   │   │ 部门/角色/用户  │
│ 主布局（NC风格） │   │ Druid 数据源    │   │ CORS 跨域配置   │   │ 前端列表+表单  │
│ 侧栏+顶栏+Tab   │   │ MyBatis 配置   │   │ Token 对接     │   │ 后端三层架构    │
│ Mock 数据       │   │ JWT + Security │   │               │   │ 全链路验收     │
│ 路由/权限守卫    │   │ SpringDoc      │   │               │   │               │
│                 │   │ 配置文件       │   │               │   │               │
└────────────────┘   └────────────────┘   └────────────────┘   └────────────────┘
```

每个 Phase 交付后可停下来评估再继续。Docker/Jenkins/DevOps 延后到业务代码稳定后再做。

---

## Maven 坐标

- groupId: `com.company.treasury`
- artifactId（父 POM）: `treasure-platform`
- 版本管理: 统一在父 POM `<dependencyManagement>` 中声明

---

## 后端模块结构

```
treasure-platform/
├── pom.xml                          # 父 POM，聚合所有子模块
├── treasure-common/                 # 公共层
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/common/
│       ├── constant/                # 系统常量、枚举
│       ├── exception/               # 统一异常定义
│       ├── result/                  # 统一返回体 Result<T>
│       ├── model/                   # 基类（BaseEntity、PageReq、PageResp）
│       └── util/                    # 通用工具类
├── treasure-datasource/             # 数据源与持久化层
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/datasource/
│       ├── config/                  # Druid 多数据源配置
│       ├── dialect/                 # SQL 方言适配（Oracle / 达梦）
│       └── mybatis/                 # MyBatis 全局配置、拦截器
├── treasure-dao/                    # 数据访问层
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/dao/
│       ├── entity/                  # 数据库实体
│       ├── mapper/                  # MyBatis Mapper 接口
│       └── resources/mapper/        # MyBatis XML 映射文件
├── treasure-security/               # 安全认证层
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/security/
│       ├── config/                  # Spring Security 配置
│       ├── filter/                  # JWT 认证过滤器
│       ├── handler/                 # 认证/授权异常处理器
│       ├── jwt/                     # JWT 令牌工具
│       └── annotation/              # 权限注解 @PreAuth
├── treasure-service/                # 业务服务层
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/service/
│       ├── api/                     # 服务接口
│       └── impl/                    # 服务实现
├── treasure-controller/             # 控制器层（REST API）
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/controller/
│       ├── system/                  # 系统管理模块
│       └── common/                  # 公共接口
├── treasure-api/                    # 对外暴露的 Feign 接口（为分布式预留）
│   ├── pom.xml
│   └── src/main/java/com/company/treasury/api/
│       └── client/                  # OpenFeign 接口定义
└── treasure-web/                    # 应用入口（可部署模块）
    ├── pom.xml
    └── src/main/java/com/company/treasury/web/
        └── TreasureApplication.java # Spring Boot 启动类
    └── src/main/resources/
        ├── application.yml          # 主配置
        ├── application-dev.yml      # 开发环境（连远程 Oracle 12c）
        ├── application-prod.yml     # 生产环境
        └── logback-spring.xml       # 日志配置
```

**设计原则**：模块按分层和未来拆分方向组织。`treasure-api` 和 `treasure-common` 是分布式预留的独立 jar；`treasure-service` 各 domain 可独立为微服务。

---

## 技术栈与版本

### 后端版本

| 组件 | 版本 | 备注 |
|------|------|------|
| Java | 17 (LTS) | Microsoft OpenJDK |
| Spring Boot | 3.2.5 | |
| Maven | 3.9.9 | 安装在 .maven/ |
| MyBatis | 3.0.3 (mybatis-spring-boot-starter) | Spring Boot 3.x 适配版 |
| Druid | 1.2.22 | druid-spring-boot-3-starter |
| PageHelper | 2.1.0 | Spring Boot 3.x 适配版 |
| SpringDoc OpenAPI | 2.5.0 | |
| Spring Security | 6.x | Spring Boot 3.2 内嵌 |
| JJWT | 0.12.5 | |
| Lombok | latest | |
| Oracle JDBC | ojdbc8:12.2.0.1 | Oracle 12c 对应驱动 |
| 达梦 JDBC | DM8 驱动 | 后续迁移时引入 |
| Maven Surefire | 3.2.x | |

---

## 命令

### 后端（treasure-platform）

```sh
# Maven 路径
export PATH="/Users/gaofengshan/workSpace/codexSpace/.maven/bin:$PATH"

# 编译全部模块
mvn compile -Dmaven.repo.local=/tmp/m2-repo -DskipTests

# 打包
mvn clean package -Dmaven.repo.local=/tmp/m2-repo -DskipTests

# 运行应用（开发模式）
mvn spring-boot:run -pl treasure-web -am -Dmaven.repo.local=/tmp/m2-repo

# 运行全部测试
mvn test -Dmaven.repo.local=/tmp/m2-repo

# 运行单模块测试
mvn test -pl treasure-service -Dmaven.repo.local=/tmp/m2-repo
```

### 前端（treasure-ui）

```sh
# 安装依赖
cd treasure-ui && npm install

# 本地开发（默认端口 5173）
npm run dev

# 生产构建
npm run build
```

### 前后端联调

```sh
# Vite proxy 转发 /api/* 到后端 8080 端口
# treasure-ui/vite.config.ts 中配置 server.proxy
# 前端跑 5173，后端跑 8080，无需 CORS 插件
```

---

## 编码规范

- 包名: `com.company.treasury.{module}.{layer}`
- 类名: 大驼峰，业务含义清晰
- REST API 路径: `/api/v1/{resource}`
- 统一响应: 所有 Controller 返回 `Result<T>` 包装
- 异常: 业务异常继承 `BizException`，由全局 `GlobalExceptionHandler` 处理
- 日志: 使用 Lombok `@Slf4j`，统一 Logback 配置
- 分页: Controller 接收 `PageReq`，Service 返回 `PageResp<T>`
- Controller 不允许直接注入 `HttpServletRequest` / `HttpServletResponse`，通过自定义注解获取用户信息

---

## 后续代理注意事项

- 所有后端依赖版本统一在父 POM `<dependencyManagement>` 中声明，子模块不写版本号
- 添加新模块需要在父 POM `<modules>` 注册
- 数据库 Schema 变更需同时提供 Oracle 和达梦两版 DDL（至少 Oracle 版 + 迁移说明）
- 前端仓库 `treasure-ui` 独立管理，前后端通过 API 约定对接
- Maven 安装在 `.maven/` 目录，不要提交，已在 .gitignore 中排除
- Docker Compose 配置放在 `deploy/` 目录下（Phase 4 后补充）
- Jenkins Pipeline 文件放在项目根目录 `Jenkinsfile`（Phase 4 后补充）
- 新代理首次介入时，务必阅读 `LESSONS_LEARNED.md` 了解项目推进过程中的经验教训
- 每次重大决策后，更新 `LESSONS_LEARNED.md` 记录经验
