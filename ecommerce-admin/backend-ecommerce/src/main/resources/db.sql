-- ============================================
-- 电商管理系统数据库脚本
-- ============================================

-- 创建数据库
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
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0未删除 1已删除',
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
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================
-- 3. 商品分类表
-- ============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `sort` int DEFAULT 0 COMMENT '排序',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ============================================
-- 4. 商品表
-- ============================================
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `description` text COMMENT '商品描述',
  `main_image` varchar(500) DEFAULT NULL COMMENT '主图',
  `status` tinyint DEFAULT 1 COMMENT '状态：0下架 1上架',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ============================================
-- 5. 订单表
-- ============================================
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint DEFAULT 0 COMMENT '订单状态：0待支付 1已支付 2已发货 3已完成 4已取消',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) DEFAULT NULL COMMENT '收货地址',
  `pay_method` varchar(20) DEFAULT NULL COMMENT '支付方式：alipay-支付宝 wechat-微信 balance-余额',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 6. 订单详情表
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
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- ============================================
-- 7. 收货地址表
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
-- 8. 购物车表
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
-- 9. 轮播图表
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
-- 10. 品牌表
-- ============================================
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `description` varchar(500) DEFAULT NULL COMMENT '品牌描述',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- ============================================
-- 11. 评价表
-- ============================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `rating` int NOT NULL DEFAULT 5 COMMENT '评分：1-5星',
  `content` text COMMENT '评价内容',
  `images` varchar(1000) DEFAULT NULL COMMENT '评价图片(JSON)',
  `status` tinyint DEFAULT 1 COMMENT '状态：0隐藏 1显示',
  `reply` varchar(500) DEFAULT NULL COMMENT '商家回复',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ============================================
-- 12. 优惠券表
-- ============================================
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '类型：1满减 2折扣 3无门槛',
  `value` decimal(10,2) NOT NULL COMMENT '优惠值',
  `min_amount` decimal(10,2) DEFAULT 0 COMMENT '最低消费金额',
  `total` int NOT NULL DEFAULT 0 COMMENT '发行总量',
  `used` int DEFAULT 0 COMMENT '已使用数量',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始',
  `end_time` datetime DEFAULT NULL COMMENT '有效期结束',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- ============================================
-- 13. 用户优惠券表
-- ============================================
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `status` tinyint DEFAULT 0 COMMENT '状态：0未使用 1已使用 2已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- ============================================
-- 14. 促销活动表
-- ============================================
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `type` tinyint NOT NULL COMMENT '类型：1满减 2折扣 3秒杀 4拼团',
  `discount_value` decimal(10,2) DEFAULT NULL COMMENT '优惠值',
  `min_amount` decimal(10,2) DEFAULT 0 COMMENT '最低消费',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用 2未开始 3已结束',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- ============================================
-- 15. 库存表
-- ============================================
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `warehouse` varchar(50) DEFAULT '默认仓库' COMMENT '仓库',
  `quantity` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `warning_quantity` int DEFAULT 10 COMMENT '预警数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- ============================================
-- 16. 库存记录表
-- ============================================
DROP TABLE IF EXISTS `inventory_record`;
CREATE TABLE `inventory_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `type` tinyint NOT NULL COMMENT '类型：1入库 2出库',
  `quantity` int NOT NULL COMMENT '数量',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存记录表';

-- ============================================
-- 17. 物流表
-- ============================================
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `company` varchar(50) NOT NULL COMMENT '快递公司',
  `tracking_no` varchar(50) NOT NULL COMMENT '快递单号',
  `status` tinyint DEFAULT 0 COMMENT '状态：0待发货 1运输中 2已签收',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流表';

-- ============================================
-- 18. 物流轨迹表
-- ============================================
DROP TABLE IF EXISTS `logistics_trace`;
CREATE TABLE `logistics_trace` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轨迹ID',
  `logistics_id` bigint NOT NULL COMMENT '物流ID',
  `content` varchar(500) NOT NULL COMMENT '轨迹内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- ============================================
-- 19. 规格表
-- ============================================
DROP TABLE IF EXISTS `specification`;
CREATE TABLE `specification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `name` varchar(50) NOT NULL COMMENT '规格名称',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `sort` int DEFAULT 0 COMMENT '排序',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规格表';

-- ============================================
-- 20. 规格值表
-- ============================================
DROP TABLE IF EXISTS `specification_value`;
CREATE TABLE `specification_value` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `spec_id` bigint NOT NULL COMMENT '规格ID',
  `value` varchar(100) NOT NULL COMMENT '规格值',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规格值表';

-- ============================================
-- 21. 菜单表
-- ============================================
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int DEFAULT 0 COMMENT '排序',
  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) DEFAULT 'M' COMMENT '菜单类型：M目录 C菜单 F按钮',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ============================================
-- 22. 操作日志表
-- ============================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module` varchar(50) DEFAULT NULL COMMENT '操作模块',
  `type` varchar(20) DEFAULT NULL COMMENT '操作类型',
  `description` varchar(200) DEFAULT NULL COMMENT '操作描述',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `status` tinyint DEFAULT 1 COMMENT '状态：0失败 1成功',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================
-- 初始数据
-- ============================================

-- 插入角色数据
INSERT INTO `role` (`id`, `role_name`, `role_key`, `status`) VALUES
(1, 'Admin', 'admin', 1),
(2, 'Operator', 'common', 1);

-- 插入管理员用户（密码：123456，使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `role_id`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Admin', 1, 1),
('operator', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Operator', 2, 1);

-- 插入商品分类
INSERT INTO `category` (`id`, `name`, `parent_id`, `sort`) VALUES
(1, 'Electronics', 0, 1),
(2, 'Clothing', 0, 2),
(3, 'Books', 0, 3),
(4, 'Phones', 1, 1),
(5, 'Computers', 1, 2),
(6, 'Mens Wear', 2, 1),
(7, 'Womens Wear', 2, 2),
(8, 'Programming', 3, 1);

-- 插入演示商品
INSERT INTO `product` (`name`, `price`, `stock`, `category_id`, `description`, `status`) VALUES
('iPhone 15 Pro', 7999.00, 100, 4, 'Apple latest phone with A17 Pro chip', 1),
('MacBook Pro 14', 14999.00, 50, 5, 'M3 Pro chip, 14-inch Liquid Retina XDR display', 1),
('Thinking in Java', 108.00, 200, 8, 'Classic Java programming book, 4th edition', 1),
('Vue.js in Action', 79.00, 150, 8, 'Vue.js framework practical guide', 1),
('Cotton T-Shirt', 99.00, 500, 6, 'Comfortable and breathable, multiple colors', 1),
('Dress', 299.00, 300, 7, 'Summer new style, elegant temperament', 1);

-- 插入演示订单
INSERT INTO `order` (`order_no`, `user_id`, `total_amount`, `status`, `receiver_name`, `receiver_phone`, `receiver_address`) VALUES
('ORD20240101001', 1, 7999.00, 3, 'Zhang San', '13800138001', 'Beijing Chaoyang District'),
('ORD20240101002', 1, 187.00, 1, 'Li Si', '13800138002', 'Shanghai Pudong District'),
('ORD20240101003', 2, 299.00, 0, 'Wang Wu', '13800138003', 'Guangzhou Tianhe District');

-- 插入订单详情
INSERT INTO `order_item` (`order_id`, `product_id`, `product_name`, `price`, `quantity`) VALUES
(1, 1, 'iPhone 15 Pro', 7999.00, 1),
(2, 3, 'Thinking in Java', 108.00, 1),
(2, 4, 'Vue.js in Action', 79.00, 1),
(3, 6, 'Dress', 299.00, 1);
