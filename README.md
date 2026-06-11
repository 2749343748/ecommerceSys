# 电商管理系统 (E-Commerce System)

一个全功能的电商系统，包含**管理后台**和**前台商城**两个子系统，采用前后端分离架构。

## 📸 项目预览

| 管理后台 - 数据看板 | 前台商城 - 首页 |
|:---:|:---:|
| 今日订单数 / 销售额 / 商品数 / 用户数统计 | 商品展示 / 轮播图 / 搜索 |
| ECharts 销售趋势图 | 商品分类浏览 |

## 🏗️ 项目结构

```
ecommerceSys/
├── ecommerce-admin/            # 管理后台
│   ├── backend-ecommerce/      # 后端服务 (Spring Boot)
│   │   ├── src/main/java/com/demo/
│   │   │   ├── controller/     # 控制器层 (REST API)
│   │   │   ├── service/        # 业务逻辑层
│   │   │   ├── mapper/         # 数据访问层 (MyBatis-Plus)
│   │   │   ├── entity/         # 实体类
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── config/         # 配置类 (安全、跨域、Knife4j等)
│   │   │   ├── common/         # 公共类 (统一响应、异常处理)
│   │   │   └── utils/          # 工具类 (JWT)
│   │   └── src/main/resources/
│   │       ├── application.yml # 应用配置
│   │       └── db.sql          # 数据库脚本
│   └── frontend-ecommerce/     # 管理端前端 (Vue 3)
│       └── src/
│           ├── views/          # 页面组件
│           ├── api/            # API 接口
│           ├── store/          # Pinia 状态管理
│           └── utils/          # 工具函数
│
└── ecommerce-mall/             # 前台商城前端 (Vue 3)
    └── src/
        ├── views/              # 页面组件
        ├── components/         # 公共组件
        ├── api/                # API 接口
        ├── store/              # Pinia 状态管理
        └── router/             # 路由配置
```

## 🛠️ 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 基础框架 |
| Java | 17 | 编程语言 |
| MyBatis-Plus | 3.5.7 | ORM 框架 |
| Spring Security | - | 安全框架 |
| JWT (JJWT) | 0.12.5 | 身份认证 |
| MySQL | - | 数据库 |
| Knife4j | 4.4.0 | 接口文档 (OpenAPI 3) |
| Hutool | 5.8.28 | Java 工具库 |
| Lombok | - | 简化代码 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5+ | 前端框架 |
| Vite | 5.4+ | 构建工具 |
| Element Plus | 2.8+ | UI 组件库 |
| Pinia | 2.2+ | 状态管理 |
| Vue Router | 4.4+ | 路由管理 |
| Axios | 1.7+ | HTTP 请求 |
| ECharts | 5.5+ | 图表库 (管理端) |

## ✨ 功能特性

### 管理后台 (ecommerce-admin)

- **数据看板** — 今日订单数、销售额、商品数、用户数统计，近7天销售趋势图
- **用户管理** — 用户列表、启用/禁用、角色分配
- **角色管理** — 角色 CRUD、权限配置
- **商品管理** — 商品增删改查、上下架、图片上传
- **商品分类** — 多级分类管理
- **品牌管理** — 品牌信息维护
- **规格管理** — 商品规格与规格值管理
- **库存管理** — 库存查询与出入库记录
- **订单管理** — 订单列表、详情、状态流转
- **优惠券管理** — 优惠券创建与发放
- **促销活动** — 促销规则配置
- **评价管理** — 商品评价审核
- **轮播图管理** — 首页 Banner 配置
- **物流管理** — 物流信息与轨迹跟踪
- **收货地址** — 地址管理
- **菜单管理** — 动态菜单配置
- **操作日志** — 系统操作记录
- **接口文档** — Knife4j 自动生成 API 文档

### 前台商城 (ecommerce-mall)

- **首页** — 轮播图、商品推荐、分类导航
- **商品浏览** — 商品列表、分类筛选、关键词搜索
- **商品详情** — 商品信息、规格选择、评价展示
- **购物车** — 加入购物车、数量修改、商品勾选
- **订单流程** — 确认订单 → 支付 → 订单列表 → 订单详情
- **用户中心** — 登录/注册、个人资料、收货地址管理

## 🚀 快速开始

### 环境要求

- **JDK** 17+
- **Maven** 3.6+
- **Node.js** 16+
- **MySQL** 5.7+ / 8.0+

### 1. 数据库初始化

```bash
# 登录 MySQL，执行建库建表脚本
mysql -u root -p < ecommerce-admin/backend-ecommerce/src/main/resources/db.sql
```

### 2. 修改数据库配置

编辑 `ecommerce-admin/backend-ecommerce/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_admin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 启动后端服务

```bash
cd ecommerce-admin/backend-ecommerce
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8088`

### 4. 启动管理端前端

```bash
cd ecommerce-admin/frontend-ecommerce
npm install
npm run dev
```

管理端前端默认运行在 `http://localhost:5173`

### 5. 启动商城前端

```bash
cd ecommerce-mall
npm install
npm run dev
```

商城前端默认运行在 `http://localhost:5174`（或 Vite 分配的其他端口）

## 📁 API 接口文档

后端启动后，访问 Knife4j 接口文档：

```
http://localhost:8088/doc.html
```

## 🗃️ 数据库设计

系统包含以下核心数据表：

| 表名 | 说明 |
|------|------|
| `user` | 用户表 |
| `role` | 角色表 |
| `menu` | 菜单表 |
| `category` | 商品分类表 |
| `product` | 商品表 |
| `brand` | 品牌表 |
| `specification` | 规格表 |
| `specification_value` | 规格值表 |
| `inventory` | 库存表 |
| `inventory_record` | 库存记录表 |
| `order` | 订单表 |
| `order_item` | 订单明细表 |
| `cart` | 购物车表 |
| `address` | 收货地址表 |
| `coupon` | 优惠券表 |
| `user_coupon` | 用户优惠券表 |
| `promotion` | 促销活动表 |
| `review` | 商品评价表 |
| `banner` | 轮播图表 |
| `logistics` | 物流表 |
| `logistics_trace` | 物流轨迹表 |
| `operation_log` | 操作日志表 |

## 📄 开源协议

本项目仅供学习交流使用。
