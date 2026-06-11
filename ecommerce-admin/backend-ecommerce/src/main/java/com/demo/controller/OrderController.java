package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.R;
import com.demo.entity.Order;
import com.demo.entity.OrderItem;
import com.demo.entity.Cart;
import com.demo.entity.Address;
import com.demo.entity.Product;
import com.demo.service.OrderService;
import com.demo.service.CartService;
import com.demo.service.AddressService;
import com.demo.service.ProductService;
import com.demo.mapper.OrderItemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 订单管理控制器
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Operation(summary = "分页查询订单列表")
    @GetMapping("/list")
    public R<IPage<Order>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) Integer status) {
        IPage<Order> page = orderService.listPage(pageNum, pageSize, keyword, status);
        return R.ok(page);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public R<Order> getDetail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        return R.ok(order);
    }

    @Operation(summary = "修改订单状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        orderService.updateStatus(id, status);
        return R.ok();
    }

    @Operation(summary = "模拟支付订单")
    @PostMapping("/pay")
    public R<?> payOrder(@RequestParam Long orderId, @RequestParam String payMethod) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            return R.fail("订单不存在");
        }
        if (order.getStatus() != 0) {
            return R.fail("订单不在待支付状态");
        }

        // 模拟支付成功，更新订单状态为已支付
        order.setStatus(1);
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        orderService.updateById(order);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("status", order.getStatus());
        return R.ok(result);
    }

    @Operation(summary = "获取统计数据")
    @GetMapping("/dashboard/total")
    public R<Map<String, Object>> getDashboardTotal() {
        Map<String, Object> result = orderService.getDashboardTotal();
        return R.ok(result);
    }

    @Operation(summary = "获取销售趋势")
    @GetMapping("/dashboard/saleTrend")
    public R<Map<String, Object>> getSaleTrend() {
        Map<String, Object> result = orderService.getSaleTrend();
        return R.ok(result);
    }

    @Operation(summary = "查询用户订单列表")
    @GetMapping("/user/list")
    public R<IPage<Order>> getUserOrders(@RequestParam Long userId,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false) Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        IPage<Order> result = orderService.page(page, wrapper);

        // 查询每个订单的商品信息
        for (Order order : result.getRecords()) {
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem> itemWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            order.setOrderItems(items);
        }

        return R.ok(result);
    }

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public R<?> createOrder(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Long addressId = Long.valueOf(params.get("addressId").toString());
        String remark = params.get("remark") != null ? params.get("remark").toString() : "";

        // 获取收货地址
        Address address = addressService.getById(addressId);
        if (address == null) {
            return R.fail("收货地址不存在");
        }

        // 获取购物车选中商品
        List<Cart> cartItems = cartService.listByUserId(userId);
        List<Cart> checkedItems = new ArrayList<>();
        for (Cart cart : cartItems) {
            if (cart.getChecked() != null && cart.getChecked() == 1) {
                checkedItems.add(cart);
            }
        }

        if (checkedItems.isEmpty()) {
            return R.fail("请选择商品");
        }

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart item : checkedItems) {
            Product product = productService.getById(item.getProductId());
            if (product == null || product.getStatus() != 1) {
                return R.fail("商品 " + item.getProductName() + " 不可购买");
            }
            if (product.getStock() < item.getQuantity()) {
                return R.fail("商品 " + product.getName() + " 库存不足");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 生成订单号
        String orderNo = "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", (int)(Math.random() * 10000));

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 待支付
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail());
        orderService.save(order);

        // 创建订单详情
        for (Cart item : checkedItems) {
            Product product = productService.getById(item.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItemMapper.insert(orderItem);

            // 扣减库存
            product.setStock(product.getStock() - item.getQuantity());
            productService.updateById(product);
        }

        // 清空购物车中已购买的商品
        for (Cart item : checkedItems) {
            cartService.removeById(item.getId());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", orderNo);
        return R.ok(result);
    }
}
