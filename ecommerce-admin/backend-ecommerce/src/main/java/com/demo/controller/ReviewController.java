package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.Review;
import com.demo.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评价管理控制器
 */
@Tag(name = "评价管理")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "分页查询评价列表")
    @GetMapping("/list")
    public R<IPage<Review>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false) Long productId,
                                  @RequestParam(required = false) Integer status) {
        IPage<Review> page = reviewService.listPage(pageNum, pageSize, productId, status);
        return R.ok(page);
    }

    @Operation(summary = "回复评价")
    @PutMapping("/reply")
    public R<?> reply(@RequestParam Long id, @RequestParam String reply) {
        reviewService.reply(id, reply);
        return R.ok();
    }

    @Operation(summary = "修改评价状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        reviewService.updateStatus(id, status);
        return R.ok();
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return R.ok();
    }
}
