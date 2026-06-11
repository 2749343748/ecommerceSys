package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.entity.Logistics;
import com.demo.entity.LogisticsTrace;
import com.demo.entity.Order;
import com.demo.mapper.LogisticsMapper;
import com.demo.mapper.LogisticsTraceMapper;
import com.demo.mapper.OrderMapper;
import com.demo.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 物流服务实现类
 */
@Service
public class LogisticsServiceImpl extends ServiceImpl<LogisticsMapper, Logistics> implements LogisticsService {

    @Autowired
    private LogisticsMapper logisticsMapper;

    @Autowired
    private LogisticsTraceMapper traceMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public IPage<Logistics> listPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Logistics> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Logistics> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Logistics::getTrackingNo, keyword);
        }
        wrapper.orderByDesc(Logistics::getCreateTime);
        IPage<Logistics> result = logisticsMapper.selectPage(page, wrapper);
        // 填充订单号
        result.getRecords().forEach(logistics -> {
            Order order = orderMapper.selectById(logistics.getOrderId());
            if (order != null) {
                logistics.setOrderNo(order.getOrderNo());
            }
        });
        return result;
    }

    @Override
    public void addLogistics(Logistics logistics) {
        logisticsMapper.insert(logistics);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Logistics logistics = logisticsMapper.selectById(id);
        logistics.setStatus(status);
        logisticsMapper.updateById(logistics);
    }

    @Override
    public void addTrace(LogisticsTrace trace) {
        traceMapper.insert(trace);
    }

    @Override
    public List<LogisticsTrace> listTraces(Long logisticsId) {
        LambdaQueryWrapper<LogisticsTrace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogisticsTrace::getLogisticsId, logisticsId)
                .orderByDesc(LogisticsTrace::getTime);
        return traceMapper.selectList(wrapper);
    }
}
