# LESSONS_LEARNED — 集团司库管理系统

本文件记录项目推进过程中的经验教训、好的决策、需要纠正的事项、以及用户偏好。
**每个新代理介入时，请先阅读此文件**，避免重复踩坑。

---

## 目录

1. [沟通风格与用户画像](#1-沟通风格与用户画像)
2. [项目启动阶段的教训](#2-项目启动阶段的教训)
3. [已纠正的假设](#3-已纠正的假设)
4. [好的决策（值得坚持）](#4-好的决策值得坚持)
5. [技术决策记录](#5-技术决策记录)
6. [需要持续关注的风险](#6-需要持续关注的风险)

---

## 1. 沟通风格与用户画像

| 维度 | 特征 |
|------|------|
| **语言偏好** | 使用中文沟通，代码标识符/命令保留英文 |
| **决策风格** | 务实主义者 — 看重当下能跑起来的东西，不过度设计 |
| **反馈习惯** | 在已有方案上直接给出修正意见（如"不用 Docker 本地 Oracle，用远程测试库"） |
| **领域背景** | 熟悉企业级系统（用友 NC Cloud），有明确的行业审美 |
| **技术能力** | 有 Java/前端基础认知，但工具链不在本地（无 Maven/Docker） |
| **节奏偏好** | 分阶段交付，每阶段可停下来评估；前端优先，快速看到视觉效果 |
| **审批敏感度** | 需要安装工具（`brew install maven`）需要主动请示；不要默认用户有全套环境 |

### 如何与这位用户高效协作

1. **先给方案，后讨论细节** — 用户喜欢看到完整的计划然后指出调整点，而不是被反复追问
2. **推荐 + 选择** — 给出 2-3 个选项并标注推荐，用户能快速决策
3. **快速出可视成果** — 前端优先的策略符合用户心理（先看到样子再补底层）
4. **不要假设用户有工具链** — 每次涉及命令行工具（maven、docker、node 等）先 check 是否存在
5. **参考行业标杆** — 用户认可成熟企业产品的设计（NC Cloud），提供行业参考比纯技术参考更有说服力

---

## 2. 项目启动阶段的教训

### 2.1 过于理想化的默认假设

这个项目的初始计划假设太多"理想条件"，导致第一版计划大范围返工：

| 初始假设 | 实际状态 | 教训 |
|----------|----------|------|
| 本地有 Maven | 无 Maven，需 Homebrew 安装 | 工具链一律先检查再假设 |
| 本地有 Docker | 无 Docker | DevOps 相关计划延后 |
| Oracle 19c | 实际是 Oracle 12c | 数据库版本要一开始就确认 |
| 有原型/PRD | 无原型，需从零设计 | 前端优先、先做布局和登录页更务实 |
| Docker Compose 本地起 Oracle | 用户有远程测试库 | 资源复用比本地模拟更好 |
| 后端基础设施优先 | 用户想要前端优先 | 开始项目前先问"你想先看到什么效果" |

### 2.2 第一版计划过重

第一版计划写了 9 天的工作量，包含太多后端基础设施 + 测试 + DevOps。
**教训**：对于从零开始的长期项目，第一计划应该聚焦在"最快能看到什么"，而非一次铺全。

---

## 3. 已纠正的假设

| 序号 | 最初方案 | 纠正后 | 原因 |
|------|----------|--------|------|
| 1 | Oracle 19c + ojdbc11 | **Oracle 12c + ojdbc8:12.2.0.1** | 用户测试环境是 12c |
| 2 | 本地 Docker 起 Oracle | **远程测试库 JDBC 直连** | 用户已有测试环境 |
| 3 | 后端基础设施优先 | **前端优先（Phase 1）** | 先看到 UI 效果更符合用户需求 |
| 4 | Swagger/SpringFox | **SpringDoc OpenAPI** | Spring Boot 3.x 不兼容 Swagger |
| 5 | Docker Compose 部署 | **DevOps 延后** | 项目稳定后再容器化 |
| 6 | oracle-jdbc:21.x | **ojdbc8:12.2.0.1** | Oracle 12c 需要对应驱动版本 |
| 7 | 单仓库前后端 | **前后端分离仓库** | 解耦更彻底 |
| 8 | Docker + Jenkins 配置 | **不生成 Docker/Jenkins 文件** | 本地无 Docker 环境 |

---

## 4. 好的决策（值得坚持）

### ✅ 架构层面

1. **多模块 Maven 结构** — 从第一天就按分层拆分模块，后续分布式迁移成本最低
2. **databaseIdProvider 方言预留** — 一开始就为达梦兼容性留好架构，避免后期大改
3. **treasure-api 预留 Feign 接口模块** — 分布式过渡时只需要往这个模块填接口即可
4. **分离前端仓库** — 前后端各自独立发布，避免单体仓库中互相阻塞

### ✅ 技术选型

5. **Spring Boot 3.2 + Java 17** — LTS 版本，生态成熟，JDK 已安装
6. **Element Plus** — Vue 3 生态最成熟的企业级组件库，表格/表单/导航齐全
7. **vue-vben-admin 技术参考** — 代码质量高，布局方案直接可复用
8. **Vite proxy 解决跨域** — 开发环境不需要后端配 CORS，简化联调流程
9. **NC Cloud 作为 UI 参考** — 用户认可行业标杆，比通用后台模板更有说服力

### ✅ 推进策略

10. **分阶段交付** — 每 Phase 可交付并评估，灵活调整后续计划
11. **Mock 数据先行** — 后端未就绪时前端可独立开发，不阻塞
12. **先问工具链状态** — 发现没有 Maven 后及时调整计划，避免生成不可用的配置

---

## 5. 技术决策记录

记录重要技术决策的背景和理由，方便后续代理理解"为什么这么选"。

### 5.1 前端优先策略

```
时间: 2026-07-06
决策: Phase 1 做前端骨架，Phase 2 做后端
理由:
  - 无系统原型，需要快速产出视觉效果供讨论
  - 用户希望先看到"做成什么样"，再决定"怎么实现"
  - Mock 数据使前端不依赖后端，可并行推进
  - 先确定 UI 能降低后续沟通成本（双方对"长什么样"有共识）
```

### 5.2 Oracle 12c + ojdbc8

```
时间: 2026-07-06
决策: 使用 ojdbc8:12.2.0.1 驱动
理由:
  - 用户测试环境为 Oracle 12c
  - ojdbc8 对应 Oracle 12.2 集群
  - ojdbc11 虽向后兼容，但锁定了更高版本协议
  - 达梦切换时只需换驱动包和连接串
```

### 5.3 NC Cloud 布局参考

```
时间: 2026-07-06
决策: 不照搬 NC Cloud，而是提取布局理念
参考要素:
  - 深色顶部导航栏（深蓝 #0d2b4e）
  - 左侧深色侧边栏 + 多级菜单
  - 数据密集的表格风格
  - KPI 卡片仪表盘
技术实现: vue-vben-admin 的布局组件方案
```

### 5.4 数据库兼容策略

```
时间: 2026-07-06
决策: MyBatis databaseIdProvider + 标准 SQL 优先
理由:
  - 达梦 DM8 支持 Oracle 兼容模式（COMPATIBLE_MODE=ORACLE）
  - 大部分 SQL 可直接复用
  - 差异部分分文件管理（xxx-oracle.xml / xxx-dm.xml）
  - PageHelper 自动识别方言，无需手写分页 SQL
```

---

## 6. 需要持续关注的风险

| 风险 | 等级 | 说明 |
|------|------|------|
| Oracle 12c 生命周期 | 🟡 中 | 12c 已过扩展支持期，未来需关注升级计划 |
| 达梦驱动兼容性 | 🟡 中 | 当前仅在架构层面预留，实际切换时需验证 |
| 无 Docker 环境 | 🟢 低 | 长期部署需要 Docker，但短期内不构成阻塞 |
| 无系统原型 | 🟢 低 | 通过前端优先策略已化解，后续按迭代逐步补齐需求 |
| 前端 Node.js 版本 | 🔴 高 | 当前 Node.js v26.4.0 较新，需确认 Element Plus/Vite 兼容性 |

---

## 更新记录

| 日期 | 更新者 | 内容 |
|------|--------|------|
| 2026-07-06 | Codex | 首次创建，记录 Phase 1 规划阶段的经验教训 |
| | | |

---

*本文件应与 AGENTS.md 一同阅读。AGENTS.md 聚焦"当前状态和规则"，本文件聚焦"我们怎么走到这里的"。*

## 里程碑记录

### 2026-07-07 — Phase 1 交付 + GitHub 初始化

| 项目 | 节点 |
|------|------|
| 后端 treasure-platform | 初始化 commit + 已推送到 GitHub |
| 前端 treasure-ui | Phase 1 骨架 45 文件 commit + 已推送到 GitHub |
| 布局验证 | 路由 bug 修复（asyncRoutes 未注册到 router）|
| Git 权限 | git push 凭据缓存，后续可由 Codex 自行管理版本 |

**关键事件**：
1. 首次构建后发现侧边栏和顶栏未显示 → 定位为 `asyncRoutes` 未合并到 router 实例 + 兜底路由抢占
2. git init 在 sandbox 中需要 `require_escalated`，已申请 prefix_rule 留用
3. GitHub HTTPS 认证需要用户手动完成首次 push 以缓存凭据
4. 两仓库使用嵌套结构：`codexSpace/`（后端）包含 `treasure-ui/`（前端子目录），通过 `.gitignore` 隔离

### 2026-07-07 — Phase 2 后端骨架交付

| 项目 | 节点 |
|------|------|
| Maven 安装 | brew install 依赖 OpenJDK 26 下载极慢；改用二进制直接解压到 .maven/ |
| Maven 本地仓库 | sandbox 阻止写 ~/.m2/repository → 用 `-Dmaven.repo.local=/tmp/m2-repo` |
| Maven 编译 | 需要 `require_escalated` 才能下载 Maven Central 依赖 |
| git push | 远程已配置 token（https://ghp_...@github.com/...），push 需 escalate |
| AGENTS.md | 需记录 Maven 命令含 `-Dmaven.repo.local` 参数 |

**重要教训**：
1. Maven 编译依赖网络下载，sandbox 默认禁止 DNS 解析 → 必须 `require_escalated` 并给足够超时（>60s）
2. MyBatis-Plus 和 MyBatis 是不同的库 —— MyBatisConfig 中误引用了 MyBatis-Plus 的类
3. `Result<T>` 的构造方法必须是 `protected` 而非 `private`，否则 `PageResult<T>` 子类无法继承
4. git push 的 token 认证（ghp_...）已经配置在 remote URL 中，但 DNS 解析在 sandbox 中不可靠，需要 escalate
5. `.maven/` 安装目录必须加入 `.gitignore`，避免将 Maven 工具提交到版本控制
6. target/ 也是构建产物，需要排除

**需要完善**：
- 可以为 `mvn compile -Dmaven.repo.local=/tmp/m2-repo` 注册 prefix_rule，避免每次重新审批
- 2026-07-07: Maven 迁移至 /Users/gaofengshan/java/maven，settings.xml 配置 localRepository，不再需要 -Dmaven.repo.local 参数
- 安装工具（Maven/Node/Docker 等）必须先问用户路径，不可擅自安装到工作目录

**改进点**：
- git push 必须在 final 回复中明确告知用户，不得默默操作
- 首次 push 到远程仓库前，应请示用户确认后再执行
- 出现网络性问题无法 push 时，清晰告知暂存状态并提供手动命令

### 2026-07-07 — Phase 3 联调 + 全局约束确立

**交付**：
- 后端完整登录 API（AuthController + JWT + CORS）
- 前端 Mock 条件开关（`VITE_USE_MOCK` 环境变量）

**全局约束记录**：
1. **3NF 数据库设计** — 所有业务表必须满足第三范式
2. **达梦兼容** — SQL 优先标准语法，差异通过 databaseIdProvider 隔离
3. **数据操作** — 建表/插数据等 DDL/DML 由 agent 直接执行，无需等用户操作
4. **开发闭环** — 改完代码后必须启动工程验证，使用 IDEA + Chrome
5. **架构演进** — Redis 缓存、微服务拆分等优化在适当时机提示用户
6. **Oracle 连接** — 配置已在 application-dev.yml 中，用户测试环境可用
