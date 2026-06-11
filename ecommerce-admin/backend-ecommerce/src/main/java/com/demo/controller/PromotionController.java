package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.Promotion;
import com.demo.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 促销活动控制器
 */
@Tag(name = "促销活动")
@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Operation(summary = "分页查询促销活动列表")
    @GetMapping("/list")
    public R<IPage<Promotion>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String keyword) {
        IPage<Promotion> page = promotionService.listPage(pageNum, pageSize, keyword);
        return R.ok(page);
    }

    @Operation(summary = "新增促销活动")
    @PostMapping
    public R<?> add(@RequestBody Promotion promotion) {
        promotionService.addPromotion(promotion);
        return R.ok();
    }

    @Operation(summary = "修改促销活动")
    @PutMapping
    public R<?> update(@RequestBody Promotion promotion) {
        promotionService.updatePromotion(promotion);
        return R.ok();
    }

    @Operation(summary = "删除促销活动")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return R.ok();
    }

    @Operation(summary = "修改活动状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        promotionService.updateStatus(id, status);
        return R.ok();
    }
}
