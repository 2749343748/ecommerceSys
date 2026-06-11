-- ============================================
-- 电商管理系统完整数据库脚本（15个功能模块）
-- ============================================

CREATE DATABASE IF NOT EXISTS `ecommerce_admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `ecommerce_admin`;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 角色表
-- ============================================
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) NOT NULL COMMENT '角色标识',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================
-- 3. 菜单表（系统管理）
-- ============================================
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int DEFAULT 0 COMMENT '排序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型：M目录 C菜单 F按钮',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ============================================
-- 4. 商品分类表
-- ============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `level` int DEFAULT 1 COMMENT '层级：1一级 2二级',
  `icon` varchar(500) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ============================================
-- 5. 品牌表
-- ============================================
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `description` text COMMENT '品牌描述',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- ============================================
-- 6. 规格表
-- ============================================
DROP TABLE IF EXISTS `specification`;
CREATE TABLE `specification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `name` varchar(100) NOT NULL COMMENT '规格名称',
  `category_id` bigint NOT NULL COMMENT '所属分类ID',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规格表';

-- ============================================
-- 7. 规格值表
-- ============================================
DROP TABLE IF EXISTS `specification_value`;
CREATE TABLE `specification_value` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `spec_id` bigint NOT NULL COMMENT '规格ID',
  `value` varchar(100) NOT NULL COMMENT '规格值',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规格值表';

-- ============================================
-- 8. 商品表
-- ============================================
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `description` text COMMENT '商品描述',
  `main_image` varchar(500) DEFAULT NULL COMMENT '主图',
  `images` text COMMENT '商品图片(JSON)',
  `sales` int DEFAULT 0 COMMENT '销量',
  `status` tinyint DEFAULT 1 COMMENT '状态：0下架 1上架',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ============================================
-- 9. 商品规格关联表
-- ============================================
DROP TABLE IF EXISTS `product_specification`;
CREATE TABLE `product_specification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `spec_id` bigint NOT NULL COMMENT '规格ID',
  `spec_value_id` bigint NOT NULL COMMENT '规格值ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格关联表';

-- ============================================
-- 10. 库存表
-- ============================================
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `warehouse` varchar(100) DEFAULT 'default' COMMENT '仓库',
  `quantity` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `warning_quantity` int DEFAULT 10 COMMENT '预警数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- ============================================
-- 11. 库存记录表
-- ============================================
DROP TABLE IF EXISTS `inventory_record`;
CREATE TABLE `inventory_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `type` tinyint NOT NULL COMMENT '类型：1入库 2出库',
  `quantity` int NOT NULL COMMENT '数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存记录表';

-- ============================================
-- 12. 收货地址表
-- ============================================
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `district` varchar(50) DEFAULT NULL COMMENT '区',
  `detail` varchar(200) NOT NULL COMMENT '详细地址',
  `is_default` tinyint DEFAULT 0 COMMENT '是否默认：0否 1是',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- ============================================
-- 13. 订单表
-- ============================================
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '实付金额',
  `discount_amount` decimal(10,2) DEFAULT 0.00 COMMENT '优惠金额',
  `coupon_id` bigint DEFAULT NULL COMMENT '优惠券ID',
  `status` tinyint DEFAULT 0 COMMENT '订单状态：0待支付 1已支付 2已发货 3已完成 4已取消',
  `pay_type` tinyint DEFAULT NULL COMMENT '支付方式：1支付宝 2微信 3银行卡',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) DEFAULT NULL COMMENT '收货地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 14. 订单详情表
-- ============================================
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `subtotal` decimal(10,2) NOT NULL COMMENT '小计',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- ============================================
-- 15. 物流表
-- ============================================
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `company` varchar(50) NOT NULL COMMENT '快递公司',
  `tracking_no` varchar(50) NOT NULL COMMENT '快递单号',
  `status` tinyint DEFAULT 0 COMMENT '物流状态：0待发货 1已发货 2运输中 3已签收',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- ============================================
-- 16. 物流轨迹表
-- ============================================
DROP TABLE IF EXISTS `logistics_trace`;
CREATE TABLE `logistics_trace` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistics_id` bigint NOT NULL COMMENT '物流ID',
  `context` varchar(500) NOT NULL COMMENT '物流详情',
  `time` datetime NOT NULL COMMENT '时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- ============================================
-- 17. 优惠券表
-- ============================================
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '类型：1满减 2折扣 3无门槛',
  `value` decimal(10,2) NOT NULL COMMENT '优惠值（金额或折扣）',
  `min_amount` decimal(10,2) DEFAULT 0.00 COMMENT '最低消费金额',
  `total` int NOT NULL COMMENT '发行总量',
  `used` int DEFAULT 0 COMMENT '已使用数量',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- ============================================
-- 18. 用户优惠券表
-- ============================================
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `status` tinyint DEFAULT 0 COMMENT '状态：0未使用 1已使用 2已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- ============================================
-- 19. 促销活动表
-- ============================================
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `type` tinyint NOT NULL COMMENT '类型：1满减 2折扣 3秒杀 4拼团',
  `discount_value` decimal(10,2) DEFAULT NULL COMMENT '优惠值',
  `min_amount` decimal(10,2) DEFAULT 0.00 COMMENT '最低消费金额',
  `product_ids` text COMMENT '参与商品ID(JSON)',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 1 COMMENT '状态：0未开始 1进行中 2已结束',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- ============================================
-- 20. 购物车表
-- ============================================
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `checked` tinyint DEFAULT 1 COMMENT '是否选中：0否 1是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 21. 评价表
-- ============================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `rating` tinyint NOT NULL COMMENT '评分：1-5星',
  `content` text COMMENT '评价内容',
  `images` text COMMENT '评价图片(JSON)',
  `status` tinyint DEFAULT 1 COMMENT '状态：0隐藏 1显示',
  `reply` text COMMENT '商家回复',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ============================================
-- 22. 轮播图表
-- ============================================
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `image` varchar(500) NOT NULL COMMENT '图片地址',
  `url` varchar(500) DEFAULT NULL COMMENT '跳转链接',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- ============================================
-- 23. 操作日志表
-- ============================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module` varchar(50) NOT NULL COMMENT '操作模块',
  `type` tinyint DEFAULT 1 COMMENT '类型：1新增 2修改 3删除 4查询',
  `description` varchar(500) NOT NULL COMMENT '操作描述',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `status` tinyint DEFAULT 1 COMMENT '状态：0失败 1成功',
  `error_msg` text COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================
-- 初始数据
-- ============================================

-- 角色
INSERT INTO `role` (`id`, `role_name`, `role_key`, `remark`, `status`) VALUES
(1, '超级管理员', 'admin', '拥有所有权限', 1),
(2, '运营人员', 'operator', '运营相关权限', 1),
(3, '客服人员', 'customer_service', '客服相关权限', 1);

-- 管理员用户（密码：123456）
INSERT INTO `user` (`username`, `password`, `nickname`, `role_id`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 1, 1),
('operator', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '运营人员', 2, 1);

-- 菜单数据
INSERT INTO `menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `icon`) VALUES
(1, '首页', 0, 1, '/dashboard', 'dashboard/index', 'C', 'dashboard:view', 'HomeFilled'),
(2, '商品管理', 0, 2, '/product', NULL, 'M', NULL, 'ShoppingCart'),
(3, '库存管理', 0, 3, '/inventory', NULL, 'M', NULL, 'Box'),
(4, '订单管理', 0, 4, '/order', NULL, 'M', NULL, 'Document'),
(5, '用户管理', 0, 5, '/user', NULL, 'M', NULL, 'User'),
(6, '营销管理', 0, 6, '/marketing', NULL, 'M', NULL, 'Present'),
(7, '内容管理', 0, 7, '/content', NULL, 'M', NULL, 'Picture'),
(8, '系统管理', 0, 8, '/system', NULL, 'M', NULL, 'Setting'),
-- 商品管理子菜单
(21, '商品分类', 2, 1, '/product/category', 'product/category/index', 'C', 'product:category:view', 'Tree'),
(22, '品牌管理', 2, 2, '/product/brand', 'product/brand/index', 'C', 'product:brand:view', 'Stamp'),
(23, '商品列表', 2, 3, '/product/list', 'product/list/index', 'C', 'product:list:view', 'List'),
(24, '规格管理', 2, 4, '/product/spec', 'product/spec/index', 'C', 'product:spec:view', 'Grid'),
-- 库存管理子菜单
(31, '库存列表', 3, 1, '/inventory/list', 'inventory/list/index', 'C', 'inventory:list:view', 'List'),
(32, '入库记录', 3, 2, '/inventory/record', 'inventory/record/index', 'C', 'inventory:record:view', 'Document'),
-- 订单管理子菜单
(41, '订单列表', 4, 1, '/order/list', 'order/list/index', 'C', 'order:list:view', 'List'),
(42, '物流管理', 4, 2, '/order/logistics', 'order/logistics/index', 'C', 'order:logistics:view', 'Van'),
-- 用户管理子菜单
(51, '用户列表', 5, 1, '/user/list', 'user/list/index', 'C', 'user:list:view', 'List'),
(52, '角色管理', 5, 2, '/user/role', 'user/role/index', 'C', 'user:role:view', 'UserFilled'),
(53, '收货地址', 5, 3, '/user/address', 'user/address/index', 'C', 'user:address:view', 'Location'),
-- 营销管理子菜单
(61, '优惠券管理', 6, 1, '/marketing/coupon', 'marketing/coupon/index', 'C', 'marketing:coupon:view', 'Ticket'),
(62, '促销活动', 6, 2, '/marketing/promotion', 'marketing/promotion/index', 'C', 'marketing:promotion:view', 'AlarmClock'),
-- 内容管理子菜单
(71, '轮播图管理', 7, 1, '/content/banner', 'content/banner/index', 'C', 'content:banner:view', 'Picture'),
(72, '评价管理', 7, 2, '/content/review', 'content/review/index', 'C', 'content:review:view', 'ChatDotRound'),
-- 系统管理子菜单
(81, '菜单管理', 8, 1, '/system/menu', 'system/menu/index', 'C', 'system:menu:view', 'Menu'),
(82, '操作日志', 8, 2, '/system/log', 'system/log/index', 'C', 'system:log:view', 'Notebook');

-- 商品分类（二级）
INSERT INTO `category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1, '数码产品', 0, 1, 1),
(2, '服装鞋帽', 0, 1, 2),
(3, '图书文具', 0, 1, 3),
(4, '手机', 1, 2, 1),
(5, '电脑', 1, 2, 2),
(6, '男装', 2, 2, 1),
(7, '女装', 2, 2, 2),
(8, '编程书籍', 3, 2, 1);

-- 品牌
INSERT INTO `brand` (`id`, `name`, `logo`, `description`, `sort`) VALUES
(1, 'Apple', '/uploads/brand/apple.png', '美国科技公司', 1),
(2, '华为', '/uploads/brand/huawei.png', '中国科技公司', 2),
(3, '优衣库', '/uploads/brand/uniqlo.png', '日本服装品牌', 3),
(4, '联想', '/uploads/brand/lenovo.png', '中国电脑品牌', 4);

-- 规格
INSERT INTO `specification` (`id`, `name`, `category_id`, `sort`) VALUES
(1, '颜色', 4, 1),
(2, '内存', 4, 2),
(3, '颜色', 6, 1),
(4, '尺码', 6, 2);

-- 规格值
INSERT INTO `specification_value` (`id`, `spec_id`, `value`, `sort`) VALUES
(1, 1, '黑色', 1),
(2, 1, '白色', 2),
(3, 1, '蓝色', 3),
(4, 2, '128GB', 1),
(5, 2, '256GB', 2),
(6, 2, '512GB', 3),
(7, 3, '黑色', 1),
(8, 3, '白色', 2),
(9, 4, 'S', 1),
(10, 4, 'M', 2),
(11, 4, 'L', 3),
(12, 4, 'XL', 4);

-- 商品
INSERT INTO `product` (`name`, `price`, `original_price`, `stock`, `category_id`, `brand_id`, `description`, `status`, `sales`) VALUES
('iPhone 15 Pro', 7999.00, 8999.00, 100, 4, 1, 'Apple最新手机，A17 Pro芯片', 1, 500),
('iPhone 15', 5999.00, 6999.00, 150, 4, 1, 'Apple手机，A16芯片', 1, 300),
('MacBook Pro 14', 14999.00, 16999.00, 50, 5, 1, 'M3 Pro芯片，14英寸', 1, 100),
('华为Mate 60', 5999.00, 6499.00, 200, 4, 2, '华为最新旗舰手机', 1, 800),
('Thinking in Java', 108.00, 128.00, 200, 8, NULL, 'Java编程经典书籍', 1, 1000),
('Cotton T-Shirt', 99.00, 199.00, 500, 6, 3, '纯棉T恤，舒适透气', 1, 2000);

-- 库存
INSERT INTO `inventory` (`product_id`, `warehouse`, `quantity`, `warning_quantity`) VALUES
(1, '默认仓库', 100, 20),
(2, '默认仓库', 150, 30),
(3, '默认仓库', 50, 10),
(4, '默认仓库', 200, 40),
(5, '默认仓库', 200, 50),
(6, '默认仓库', 500, 100);

-- 优惠券
INSERT INTO `coupon` (`name`, `type`, `value`, `min_amount`, `total`, `used`, `start_time`, `end_time`) VALUES
('新人券-满100减10', 1, 10.00, 100.00, 1000, 50, '2024-01-01', '2025-12-31'),
('满500减50', 1, 50.00, 500.00, 500, 100, '2024-01-01', '2025-12-31'),
('9折优惠券', 2, 0.90, 0.00, 200, 20, '2024-01-01', '2025-12-31');

-- 促销活动
INSERT INTO `promotion` (`name`, `type`, `discount_value`, `min_amount`, `start_time`, `end_time`, `status`) VALUES
('新年大促', 1, 50.00, 300.00, '2024-01-01', '2024-01-31', 2),
('周末特惠', 2, 0.85, 0.00, '2024-01-01', '2025-12-31', 1);

-- 轮播图
INSERT INTO `banner` (`title`, `image`, `url`, `sort`) VALUES
('新年大促', '/uploads/banner/banner1.jpg', '/promotion/1', 1),
('新品上市', '/uploads/banner/banner2.jpg', '/product/1', 2),
('限时折扣', '/uploads/banner/banner3.jpg', '/promotion/2', 3);
