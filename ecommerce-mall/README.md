# 品质商城 - 电商网站

基于 Vue 3 + Vite 构建的现代化电商网站，与后台管理系统共享同一后端 API。

## 技术栈

- **前端框架**: Vue 3 + Vite
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP 客户端**: Axios
- **UI 组件**: Element Plus (部分使用)
- **样式**: 自定义 CSS (简约现代风)

## 项目结构

```
src/
├── api/            # API 接口
├── assets/         # 静态资源
├── components/     # 公共组件
├── router/         # 路由配置
├── store/          # 状态管理
├── utils/          # 工具函数
└── views/          # 页面组件
    ├── home/       # 首页
    ├── product/    # 商品相关
    ├── cart/       # 购物车
    ├── order/      # 订单相关
    ├── user/       # 用户相关
    └── search/     # 搜索
```

## 功能模块

- **首页**: 轮播图、分类导航、热销商品、新品上架
- **商品**: 列表浏览、分类筛选、详情查看、评价查看
- **购物车**: 添加商品、数量调整、全选/单选、删除
- **订单**: 确认订单、订单列表、订单详情、取消/确认收货
- **用户**: 登录、注册、个人中心、收货地址管理
- **搜索**: 关键词搜索商品

## 启动方式

### 1. 启动后端

```bash
cd D:\A_MyProject\ecommerce-admin\backend-ecommerce
mvn spring-boot:run
```

后端运行在 http://localhost:8088

### 2. 启动前端

```bash
cd D:\A_MyProject\ecommerce-mall
npm install
npm run dev
```

前端运行在 http://localhost:5174

## API 对接

本项目与后台管理系统共享同一后端 API，主要接口：

| 功能 | 接口 | 说明 |
|------|------|------|
| 登录 | POST /api/login | 用户登录 |
| 注册 | POST /api/register | 用户注册 |
| 商品列表 | GET /api/product/list | 分页查询商品 |
| 商品详情 | GET /api/product/{id} | 获取商品详情 |
| 分类树 | GET /api/category/tree | 获取分类树 |
| 购物车 | GET/POST/PUT/DELETE /api/cart/* | 购物车操作 |
| 订单 | POST /api/order/create | 创建订单 |
| 地址 | GET/POST/PUT/DELETE /api/address/* | 收货地址管理 |

## 测试账号

- 用户名: admin
- 密码: 123456

或注册新账号进行测试。
