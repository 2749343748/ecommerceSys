package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.dto.BrandDTO;
import com.demo.entity.Brand;
import com.demo.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 品牌管理控制器
 */
@Tag(name = "品牌管理")
@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(summary = "分页查询品牌列表")
    @GetMapping("/list")
    public R<IPage<Brand>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String keyword) {
        IPage<Brand> page = brandService.listPage(pageNum, pageSize, keyword);
        return R.ok(page);
    }

    @Operation(summary = "新增品牌")
    @PostMapping
    public R<?> add(@RequestBody @Valid BrandDTO brandDTO) {
        brandService.addBrand(brandDTO);
        return R.ok();
    }

    @Operation(summary = "修改品牌")
    @PutMapping
    public R<?> update(@RequestBody @Valid BrandDTO brandDTO) {
        brandService.updateBrand(brandDTO);
        return R.ok();
    }

    @Operation(summary = "删除品牌")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return R.ok();
    }

    @Operation(summary = "修改品牌状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        brandService.updateStatus(id, status);
        return R.ok();
    }
}
