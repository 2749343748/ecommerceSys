package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.entity.OperationLog;
import com.demo.mapper.OperationLogMapper;
import com.demo.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 操作日志服务实现类
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper logMapper;

    @Override
    public IPage<OperationLog> listPage(Integer pageNum, Integer pageSize, String module) {
        Page<OperationLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(module)) {
            wrapper.eq(OperationLog::getModule, module);
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return logMapper.selectPage(page, wrapper);
    }

    @Override
    public void recordLog(String module, Integer type, String description, String method, String params, Integer status) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setType(type);
        log.setDescription(description);
        log.setMethod(method);
        log.setParams(params);
        log.setStatus(status);
        logMapper.insert(log);
    }
}
