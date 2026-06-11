package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Order;
import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.mapper.OrderMapper;
import com.demo.mapper.ProductMapper;
import com.demo.mapper.UserMapper;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public IPage<Order> listPage(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Order::getOrderNo, keyword)
                    .or().like(Order::getReceiverName, keyword);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Order getOrderDetail(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(status);
        orderMapper.updateById(order);
    }

    @Override
    public Map<String, Object> getDashboardTotal() {
        Map<String, Object> result = new HashMap<>();

        // 今日订单数
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LambdaQueryWrapper<Order> todayOrderWrapper = new LambdaQueryWrapper<>();
        todayOrderWrapper.between(Order::getCreateTime, todayStart, todayEnd);
        long todayOrderCount = orderMapper.selectCount(todayOrderWrapper);
        result.put("todayOrderCount", todayOrderCount);

        // 今日销售额
        LambdaQueryWrapper<Order> todaySaleWrapper = new LambdaQueryWrapper<>();
        todaySaleWrapper.between(Order::getCreateTime, todayStart, todayEnd)
                .in(Order::getStatus, 1, 2, 3); // 已支付、已发货、已完成
        List<Order> todayOrders = orderMapper.selectList(todaySaleWrapper);
        double todaySaleAmount = todayOrders.stream()
                .mapToDouble(o -> o.getTotalAmount().doubleValue())
                .sum();
        result.put("todaySaleAmount", todaySaleAmount);

        // 总商品数
        long totalProductCount = productMapper.selectCount(null);
        result.put("totalProductCount", totalProductCount);

        // 总用户数
        long totalUserCount = userMapper.selectCount(null);
        result.put("totalUserCount", totalUserCount);

        return result;
    }

    @Override
    public Map<String, Object> getSaleTrend() {
        Map<String, Object> result = new HashMap<>();

        // 最近7天的日期和销售额
        String[] dates = new String[7];
        double[] amounts = new double[7];

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates[6 - i] = date.format(DateTimeFormatter.ofPattern("MM-dd"));

            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(Order::getCreateTime, dayStart, dayEnd)
                    .in(Order::getStatus, 1, 2, 3);

            List<Order> orders = orderMapper.selectList(wrapper);
            amounts[6 - i] = orders.stream()
                    .mapToDouble(o -> o.getTotalAmount().doubleValue())
                    .sum();
        }

        result.put("dates", dates);
        result.put("amounts", amounts);

        return result;
    }
}
