# AGENTS.md

本文件为在本仓库中工作的 AI 编程代理提供协作规范。
同时也是 **treasure-platform**（集团司库管理系统后端）的项目技术说明。

---

## 项目概述

| 项目 | 说明 |
|------|------|
| 项目名称 | 集团司库管理系统 |
| 后端仓库 | treasure-platform（本仓库） |
| 前端仓库 | treasure-ui（独立仓库，另建） |
| 策略路线 | **前端优先** — 先做 UI 骨架与登录页，后端逐步补齐 |
| 建设阶段 | 4 个 Phase，详见下方「落地路线图」 |

### 后端

| 项 | 取值 |
|----|------|
| 语言 | Java 17 (Microsoft OpenJDK) |
| 框架 | Spring Boot 3.2.x |
| 构建 | Maven 多模块（通过 Homebrew 安装） |
| 数据层 | MyBatis 3 + Druid + PageHelper |
| 数据库 | Oracle 12c（远程测试环境） |
| 未来迁移 | 达梦 DM8（已预留方言适配架构） |
| API 文档 | SpringDoc OpenAPI 2.x |
| 认证 | Spring Security + JWT |

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

| 工具 | 状态 | 安装方式 |
|------|------|----------|
| Java 17 | 已安装 | Microsoft OpenJDK 17.0.19 LTS |
| Node.js 26 | 已安装 | v26.4.0 |
| npm | 已安装 | 11.17.0 |
| Homebrew | 已安装 | 6.0.6 |
| Maven | 待安装 | `brew install maven`（Phase 2 时安装） |
| Docker | 待安装 | Phase 4 后再考虑 |
| Oracle | 远程测试环境 | 12c，通过 JDBC 直连 |

---

## 落地路线图（4 Phase）

```
Phase 1              Phase 2              Phase 3              Phase 4
┌────────────────┐   ┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│ 前端骨架        │ → │ 后端骨架        │ → │ 前后端联调      │ → │ 示例业务模块    │
│                │   │                │   │                │   │                │
│ Vue 3 项目创建  │   │ brew install   │   │ Mock 切真实 API │   │ 用户管理 CRUD  │
│ 登录页设计       │   │   maven        │   │ 登录流程全链路   │   │ 部门/角色/用户  │
│ 主布局（NC风格） │   │ 8 模块 POM 结构 │   │ CORS 跨域配置   │   │ 前端列表+表单  │
│ 侧栏+顶栏+Tab   │   │ Druid 数据源    │   │ Token 对接     │   │ 后端三层架构    │
│ Mock 数据       │   │ MyBatis 配置   │   │               │   │ 全链路验收     │
│ 路由/权限守卫    │   │ JWT + Security │   │               │   │               │
│                 │   │ SpringDoc      │   │               │   │               │
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

## 前端项目结构（treasure-ui，独立仓库）

```
treasure-ui/
├── src/
│   ├── api/                     # API 接口定义
│   │   ├── system/              #   系统管理模块
│   │   └── ...
│   ├── assets/                  # 静态资源
│   ├── components/              # 公共组件
│   │   ├── AppLogo.vue          #   顶部 Logo
│   │   ├── AppSearch.vue        #   全局搜索
│   │   └── ...
│   ├── layouts/                 # 布局组件
│   │   ├── BasicLayout.vue      #   主布局（侧栏+顶栏+内容）
│   │   ├── Sidebar/             #   侧边栏组件
│   │   ├── Header/              #   顶部导航组件
│   │   └── TabsView/            #   多标签页组件
│   ├── views/                   # 页面
│   │   ├── login/               #   登录页
│   │   ├── dashboard/           #   首页仪表盘
│   │   ├── account/             #   账户管理
│   │   ├── fund/                #   资金归集
│   │   ├── payment/             #   支付结算
│   │   ├── budget/              #   资金预算
│   │   ├── report/              #   报表分析
│   │   └── system/              #   系统管理
│   ├── router/                  # 路由配置（含路由守卫）
│   ├── stores/                  # Pinia 状态
│   │   ├── app.ts               #   应用配置（侧边栏折叠、主题）
│   │   ├── user.ts              #   用户与权限
│   │   └── tabs.ts              #   多标签页状态
│   ├── utils/                   # 工具函数
│   │   └── request.ts           #   Axios 封装
│   ├── mock/                    # Mock 数据
│   │   ├── login.ts             #   登录模拟（admin/admin123）
│   │   └── user.ts              #   用户信息 + 菜单权限
│   ├── styles/                  # 全局样式
│   │   ├── variables.scss       #   Element Plus 主题变量覆盖
│   │   └── index.scss           #   全局样式
│   ├── App.vue
│   └── main.ts
├── vite.config.ts
├── tsconfig.json
├── package.json
└── Dockerfile                   # 后续再实现
```

---

## 技术栈与版本

### 后端版本

| 组件 | 版本 | 备注 |
|------|------|------|
| Java | 17 (LTS) | |
| Spring Boot | 3.2.x | |
| Maven | 3.9+ | Phase 2 时 `brew install maven` |
| MyBatis | 3.0.3 (mybatis-spring-boot-starter) | Spring Boot 3.x 适配版 |
| Druid | 1.2.22 | |
| PageHelper | 2.1.0 | Spring Boot 3.x 适配版 |
| SpringDoc OpenAPI | 2.5.x | |
| Spring Security | 6.x | Spring Boot 3.2 内嵌 |
| JJWT | 0.12.x | |
| Lombok | latest | |
| Oracle JDBC | **ojdbc8:12.2.0.1** | Oracle 12c 对应驱动 |
| 达梦 JDBC | DM8 驱动 | 后续迁移时引入 |
| Maven Surefire | 3.2.x | |

### 前端版本

| 组件 | 版本 |
|------|------|
| Vue | 3.x |
| TypeScript | 5.x |
| Vite | 6.x |
| Element Plus | latest |
| Pinia | latest |
| Vue Router | 4.x |
| Axios | latest |
| @iconify/vue | latest |
| mockjs | latest |
| vite-plugin-mock | latest |

---

## 数据库兼容策略（Oracle 12c ↔ 达梦 DM8）

1. **优先使用标准 SQL** — 避免 Oracle 专属语法（`SELECT ... FROM DUAL`、`NVL`、`DECODE`、`CONNECT BY`）
2. **MyBatis databaseIdProvider** — 需要差异的 SQL 按 databaseId 分文件（`xxx-oracle.xml` / `xxx-dm.xml`）
3. **达梦兼容模式** — DMSERVER 配置 `COMPATIBLE_MODE=ORACLE` 降低迁移成本
4. **序列与自增** — 统一使用 MyBatis `selectKey` + 序列（兼容两边）
5. **分页** — 通过 PageHelper 内置方言适配，无需手写分页 SQL
6. **数据类型** — 避免 Oracle 特有类型（CLOB 用 VARCHAR2(4000) + 长文本分表处理）

---

## 布局参考（用友 NC Cloud 改良版）

- **顶部深蓝导航栏** — 左侧 Logo + 系统名称，右侧通知铃铛 + 用户头像下拉
- **左侧深色侧边栏** — 多级菜单，支持折叠为 icon 模式
- **多标签页** — vue-vben-admin 标签页方案，可关闭/刷新 Tab
- **数据表格** — Element Plus `<el-table>` 紧凑模式，密集数据展示
- **KPI 卡片** — 首页展示资金余额、今日收支、待审批数量等关键指标

### 主题色

```scss
$primary-color: #1a5c8a;       // NC Cloud 标志性深蓝
$header-bg: #0d2b4e;           // 顶部导航深蓝背景
$sidebar-bg: #1e293b;          // 侧边栏深色背景
$sidebar-active: #1a5c8a;      // 菜单激活色
$page-bg: #f0f2f5;             // 页面背景浅灰
```

---

## 命令

### 前端（treasure-ui）

```sh
# 安装依赖
npm install

# 本地开发（默认端口 5173）
npm run dev

# 生产构建
npm run build

# 代码检查
npm run lint
```

### 后端（treasure-platform）

```sh
# 安装 Maven
brew install maven

# 编译全部模块（跳过测试）
mvn clean compile -DskipTests

# 打包
mvn clean package -DskipTests

# 运行应用（开发模式，连远程 Oracle 12c）
mvn spring-boot:run -pl treasure-web -am

# 运行全部测试
mvn test

# 运行单模块测试
mvn test -pl treasure-service
```

### 前后端联调

```sh
# 方式：Vite proxy 转发 /api/* 到后端 8080 端口
# vue-treasure/vite.config.ts 中配置 server.proxy
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
- 前端组件名: 大驼峰 PascalCase
- 前端文件名: 与组件名一致
- 前端 API 路径: `src/api/{module}/{resource}.ts`

---

## 测试要求

### 后端

- Service 层编写单元测试，使用 Mockito + JUnit 5
- Controller 层编写 `@WebMvcTest` 切片测试
- DAO 层编写 `@MybatisTest`（使用 H2 内存数据库模拟）
- 修改公共模块（common / datasource / security）必须补充对应测试
- 数据源切换后需运行全量测试确保多方言兼容性

### 前端

- 核心工具函数（request.ts、路由守卫）添加单元测试
- 登录页等关键交互通过手动验证确认
- Mock 模式与真实 API 模式切换验证

---

## 后续代理注意事项

- 所有后端依赖版本统一在父 POM `<dependencyManagement>` 中声明，子模块不写版本号
- 添加新模块需要在父 POM `<modules>` 注册
- 数据库 Schema 变更需同时提供 Oracle 和达梦两版 DDL（至少 Oracle 版 + 迁移说明）
- 前端仓库 `treasure-ui` 独立管理，前后端通过 API 约定对接
- Docker Compose 配置放在 `deploy/` 目录下（Phase 4 后补充）
- Jenkins Pipeline 文件放在项目根目录 `Jenkinsfile`（Phase 4 后补充）
- **重要**：AGENTS.md 中的 Oracle JDBC 版本为 `ojdbc8:12.2.0.1`（对应 Oracle 12c），不可随意升级为 ojdbc11
- **重要**：本地无 Docker 环境，不要生成 Docker 相关文件，除非用户明确要求
- 新代理首次介入时，务必阅读 `LESSONS_LEARNED.md` 了解项目推进过程中的经验教训
- 每次重大决策后，更新 `LESSONS_LEARNED.md` 记录经验
