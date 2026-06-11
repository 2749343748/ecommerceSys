# 电商管理系统 — 使用手册

## 目录

- [系统简介](#系统简介)
- [环境要求](#环境要求)
- [安装部署](#安装部署)
- [功能说明](#功能说明)
- [配置说明](#配置说明)
- [常见问题](#常见问题)

---

## 系统简介

电商管理系统是一个基于 Spring Boot 3 + Vue 3 + Element Plus 的后台管理平台，提供商品管理、库存管控、订单处理、用户管理、营销推广、内容管理、系统配置等完整的电商业务功能。

---

## 环境要求

| 依赖 | 版本要求 |
|------|----------|
| JDK | 17+ |
| Node.js | 16+ |
| MySQL | 5.7+ |
| Maven | 3.6+ |

---

## 安装部署

### 1. 获取项目

```bash
git clone <项目仓库地址>
cd ecommerce-admin
```

### 2. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE ecommerce_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 导入数据
mysql -u root -p ecommerce_admin < backend-ecommerce/src/main/resources/db.sql
```

### 3. 配置后端

编辑 `backend-ecommerce/src/main/resources/application.yml`，修改数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_admin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456  # 改为实际密码
```

### 4. 启动后端

```bash
cd backend-ecommerce
mvn spring-boot:run
```

启动成功后控制台会显示 `Tomcat started on port(s): 8088`

### 5. 启动前端

```bash
cd frontend-ecommerce
npm install
npm run dev
```

启动成功后访问 http://localhost:5173

### 6. 登录系统

打开浏览器访问 http://localhost:5173，输入默认账号登录：

| 账号 | 密码 |
|------|------|
| admin | 123456 |

---

## 功能说明

### 登录认证

- 输入用户名和密码登录系统
- 登录后自动跳转到首页
- 未登录访问任意页面会自动跳转到登录页
- Token 有效期 24 小时，过期后需重新登录

### 首页仪表盘

登录后默认进入首页，展示：
- 今日订单数、今日销售额
- 商品总数、用户总数
- 近 7 天销售趋势图

### 商品管理

#### 商品分类

管理商品的分类体系，支持树形结构：

- 点击「新增」添加一级或子分类
- 点击「编辑」修改分类名称
- 点击「删除」移除分类（需先删除其子分类）

#### 品牌管理

管理商品品牌信息：

- 支持品牌名称搜索
- 新增/编辑品牌名称和图片
- 删除未使用的品牌

#### 规格管理

管理商品规格（如颜色、尺码等）：

- 新增规格名称（如「颜色」）
- 为规格添加规格值（如「红色」「蓝色」）

#### 商品列表

管理所有商品信息：

- 支持按名称搜索、按分类筛选
- 点击「新增」创建商品，填写名称、价格、库存、分类、品牌、规格、图片、描述
- 点击「编辑」修改商品信息
- 点击「上架/下架」切换商品状态
- 点击「删除」移除商品

### 库存管理

#### 库存列表

查看所有商品的库存情况：

- 显示商品名称、规格、当前库存
- 支持按商品名称搜索

#### 入库记录

查看历史入库记录：

- 记录包含商品名称、入库数量、入库时间
- 支持按时间范围筛选

### 订单管理

#### 订单列表

管理所有订单：

- 支持按订单号搜索
- 支持按订单状态筛选（待付款、待发货、已发货、已完成、已取消）
- 点击「查看」查看订单详情（商品信息、收货地址、金额）
- 点击「发货」更新订单状态为已发货

#### 物流管理

管理订单物流信息：

- 为已发货订单填写物流公司和运单号
- 查看物流状态

### 用户管理

#### 用户列表

管理平台用户：

- 支持按用户名、手机号搜索
- 点击「新增」创建用户，设置用户名、密码、手机号、角色
- 点击「编辑」修改用户信息
- 点击「删除」移除用户

#### 角色管理

管理用户角色和权限：

- 新增角色并分配菜单权限
- 缒辑角色名称和权限
- 删除未分配的角色

#### 收货地址

查看所有用户的收货地址信息：

- 支持按用户名搜索
- 查看地址详情

### 营销管理

#### 优惠券管理

创建和管理优惠券：

- 新增优惠券：设置名称、类型（满减/折扣）、面额、使用门槛、有效期、发放数量
- 编辑未使用的优惠券
- 查看优惠券使用统计

#### 促销活动

管理促销活动：

- 新增活动：设置名称、时间、折扣规则
- 编辑/删除活动
- 启用/停用活动

### 内容管理

#### 轮播图管理

管理首页轮播图：

- 上传轮播图图片
- 设置跳转链接
- 调整显示顺序
- 启用/禁用轮播图

#### 评价管理

管理商品评价：

- 查看用户评价内容和评分
- 删除不当评价
- 回复用户评价

### 系统管理

#### 菜单管理

配置系统菜单和权限：

- 新增菜单项：设置名称、路径、图标、排序
- 编辑菜单信息
- 删除未使用的菜单
- 支持多级菜单结构

#### 操作日志

查看系统操作记录：

- 记录包含操作人、操作内容、操作时间
- 支持按时间范围和操作类型筛选

---

## 配置说明

### 后端配置

配置文件位于 `backend-ecommerce/src/main/resources/application.yml`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `server.port` | 8088 | 服务端口 |
| `spring.datasource.url` | - | MySQL 连接地址 |
| `spring.datasource.username` | root | 数据库用户名 |
| `spring.datasource.password` | root | 数据库密码 |
| `jwt.secret` | - | JWT 密钥（至少 32 字符） |
| `jwt.expiration` | 86400000 | Token 过期时间（毫秒，默认 24 小时） |
| `file.upload.path` | ./upload/ | 文件上传存储路径 |
| `file.upload.url` | http://localhost:8088/upload/ | 文件访问地址 |
| `spring.servlet.multipart.max-file-size` | 10MB | 单文件上传大小限制 |

### 前端配置

配置文件位于 `frontend-ecommerce/vite.config.js`

开发环境下，前端通过 Vite 代理转发请求：

| 代理路径 | 目标地址 |
|----------|----------|
| `/api` | `http://localhost:8088` |
| `/upload` | `http://localhost:8088` |

如需修改后端地址，编辑 `vite.config.js` 中的 `proxy.target`。

---

## 常见问题

### 数据库连接失败

**现象**：后端启动报错 `Communications link failure`

**解决**：
1. 确认 MySQL 服务已启动
2. 检查 `application.yml` 中的数据库地址、用户名、密码
3. 确认数据库 `ecommerce_admin` 已创建

### 图片上传失败

**现象**：上传图片返回错误

**解决**：
1. 确认项目根目录下存在 `upload` 文件夹
2. 确认该文件夹有写入权限
3. 检查 `application.yml` 中 `file.upload.path` 配置

### 跨域请求被拒绝

**现象**：前端请求返回 CORS 错误

**解决**：
- 开发环境：Vite 已配置代理，正常情况下不会出现
- 生产环境：需在 Nginx 配置反向代理，或在后端 `CorsConfig.java` 中配置允许的域名

### Knife4j 文档无法访问

**现象**：访问 http://localhost:8088/doc.html 显示 404

**解决**：
1. 确认后端已启动
2. 检查 `application.yml` 中 Knife4j 配置是否启用
3. 尝试访问 http://localhost:8088/swagger-ui.html

### 端口被占用

**现象**：启动时报端口 8088 或 5173 已被占用

**解决**：
```bash
# Windows 查看端口占用
netstat -ano | findstr :8088

# 修改后端端口：编辑 application.yml 中的 server.port
# 修改前端端口：编辑 vite.config.js 中的 server.port
```

### 生产环境部署

开发模式（`npm run dev`）不适合生产环境，部署步骤：

```bash
# 1. 打包前端
cd frontend-ecommerce
npm run build  # 生成 dist 目录

# 2. 打包后端
cd backend-ecommerce
mvn clean package -DskipTests  # 生成 target/ecommerce-admin-1.0.0.jar

# 3. 运行后端
java -jar target/ecommerce-admin-1.0.0.jar

# 4. 配置 Nginx 托管前端 dist 目录，并反向代理 /api 到后端
```

Nginx 参考配置：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/frontend-ecommerce/dist;
        try_files $uri $uri/ /index.html;
    }

    # 后端接口代理
    location /api {
        proxy_pass http://localhost:8088;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # 上传文件访问
    location /upload {
        proxy_pass http://localhost:8088;
    }
}
```

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 3.2.5 / Java 17 |
| 安全 | Spring Security + JWT |
| ORM | MyBatis-Plus 3.5.7 |
| 文档 | Knife4j 4.4.0 (OpenAPI 3.0) |
| 数据库 | MySQL 5.7 / 8.0 |
| 前端 | Vue 3.5 + Vite 5.4 |
| UI | Element Plus 2.8 |
| 状态管理 | Pinia 2.2 |
| 图表 | ECharts 5.5 |

## License

MIT
