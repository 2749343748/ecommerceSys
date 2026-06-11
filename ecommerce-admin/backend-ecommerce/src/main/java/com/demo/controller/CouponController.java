package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.Coupon;
import com.demo.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 优惠券控制器
 */
@Tag(name = "优惠券管理")
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Operation(summary = "分页查询优惠券列表")
    @GetMapping("/list")
    public R<IPage<Coupon>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false) String keyword) {
        IPage<Coupon> page = couponService.listPage(pageNum, pageSize, keyword);
        return R.ok(page);
    }

    @Operation(summary = "新增优惠券")
    @PostMapping
    public R<?> add(@RequestBody Coupon coupon) {
        couponService.addCoupon(coupon);
        return R.ok();
    }

    @Operation(summary = "修改优惠券")
    @PutMapping
    public R<?> update(@RequestBody Coupon coupon) {
        couponService.updateCoupon(coupon);
        return R.ok();
    }

    @Operation(summary = "删除优惠券")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return R.ok();
    }

    @Operation(summary = "修改优惠券状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        couponService.updateStatus(id, status);
        return R.ok();
    }
}
