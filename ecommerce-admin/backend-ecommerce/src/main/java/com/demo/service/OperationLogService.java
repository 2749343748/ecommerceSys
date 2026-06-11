package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 分页查询操作日志
     */
    IPage<OperationLog> listPage(Integer pageNum, Integer pageSize, String module);

    /**
     * 记录操作日志
     */
    void recordLog(String module, Integer type, String description, String method, String params, Integer status);
}
