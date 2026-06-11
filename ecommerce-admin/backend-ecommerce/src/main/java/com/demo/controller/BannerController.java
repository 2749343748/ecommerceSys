package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.entity.Banner;
import com.demo.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 轮播图控制器
 */
@Tag(name = "轮播图管理")
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Operation(summary = "分页查询轮播图列表")
    @GetMapping("/list")
    public R<IPage<Banner>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Banner> page = bannerService.listPage(pageNum, pageSize);
        return R.ok(page);
    }

    @Operation(summary = "新增轮播图")
    @PostMapping
    public R<?> add(@RequestBody Banner banner) {
        bannerService.addBanner(banner);
        return R.ok();
    }

    @Operation(summary = "修改轮播图")
    @PutMapping
    public R<?> update(@RequestBody Banner banner) {
        bannerService.updateBanner(banner);
        return R.ok();
    }

    @Operation(summary = "删除轮播图")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return R.ok();
    }

    @Operation(summary = "修改轮播图状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        bannerService.updateStatus(id, status);
        return R.ok();
    }
}
