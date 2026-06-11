package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Logistics;
import com.demo.entity.LogisticsTrace;

import java.util.List;

/**
 * 物流服务接口
 */
public interface LogisticsService extends IService<Logistics> {

    /**
     * 分页查询物流列表
     */
    IPage<Logistics> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 新增物流信息
     */
    void addLogistics(Logistics logistics);

    /**
     * 修改物流状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 添加物流轨迹
     */
    void addTrace(LogisticsTrace trace);

    /**
     * 查询物流轨迹
     */
    List<LogisticsTrace> listTraces(Long logisticsId);
}
