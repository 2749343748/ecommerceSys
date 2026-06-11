package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Order;

import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 分页查询订单
     */
    IPage<Order> listPage(Integer pageNum, Integer pageSize, String keyword, Integer status);

    /**
     * 获取订单详情
     */
    Order getOrderDetail(Long id);

    /**
     * 修改订单状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取统计数据
     */
    Map<String, Object> getDashboardTotal();

    /**
     * 获取销售趋势
     */
    Map<String, Object> getSaleTrend();
}
