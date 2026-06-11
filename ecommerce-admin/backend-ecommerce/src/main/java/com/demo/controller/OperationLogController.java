package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.OperationLog;
import com.demo.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/api/log")
public class OperationLogController {

    @Autowired
    private OperationLogService logService;

    @Operation(summary = "分页查询操作日志")
    @GetMapping("/list")
    public R<IPage<OperationLog>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String module) {
        IPage<OperationLog> page = logService.listPage(pageNum, pageSize, module);
        return R.ok(page);
    }
}
