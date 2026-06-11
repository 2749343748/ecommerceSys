package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.Logistics;
import com.demo.entity.LogisticsTrace;
import com.demo.service.LogisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物流管理控制器
 */
@Tag(name = "物流管理")
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    @Operation(summary = "分页查询物流列表")
    @GetMapping("/list")
    public R<IPage<Logistics>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String keyword) {
        IPage<Logistics> page = logisticsService.listPage(pageNum, pageSize, keyword);
        return R.ok(page);
    }

    @Operation(summary = "新增物流信息")
    @PostMapping
    public R<?> add(@RequestBody Logistics logistics) {
        logisticsService.addLogistics(logistics);
        return R.ok();
    }

    @Operation(summary = "修改物流状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        logisticsService.updateStatus(id, status);
        return R.ok();
    }

    @Operation(summary = "添加物流轨迹")
    @PostMapping("/trace")
    public R<?> addTrace(@RequestBody LogisticsTrace trace) {
        logisticsService.addTrace(trace);
        return R.ok();
    }

    @Operation(summary = "查询物流轨迹")
    @GetMapping("/traces")
    public R<List<LogisticsTrace>> listTraces(@RequestParam Long logisticsId) {
        List<LogisticsTrace> list = logisticsService.listTraces(logisticsId);
        return R.ok(list);
    }
}
