package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.dto.InventoryDTO;
import com.demo.entity.Inventory;
import com.demo.entity.InventoryRecord;
import com.demo.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存管理控制器
 */
@Tag(name = "库存管理")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "分页查询库存列表")
    @GetMapping("/list")
    public R<IPage<Inventory>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String keyword) {
        IPage<Inventory> page = inventoryService.listPage(pageNum, pageSize, keyword);
        return R.ok(page);
    }

    @Operation(summary = "入库")
    @PostMapping("/in")
    public R<?> stockIn(@RequestBody InventoryDTO dto) {
        inventoryService.stockIn(dto.getProductId(), dto.getQuantity(), dto.getRemark());
        return R.ok();
    }

    @Operation(summary = "出库")
    @PostMapping("/out")
    public R<?> stockOut(@RequestBody InventoryDTO dto) {
        inventoryService.stockOut(dto.getProductId(), dto.getQuantity(), dto.getRemark());
        return R.ok();
    }

    @Operation(summary = "分页查询库存记录")
    @GetMapping("/records")
    public R<IPage<InventoryRecord>> listRecords(@RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) Long productId) {
        IPage<InventoryRecord> page = inventoryService.listRecords(pageNum, pageSize, productId);
        return R.ok(page);
    }
}
