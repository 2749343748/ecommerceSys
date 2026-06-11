package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.dto.ProductDTO;
import com.demo.entity.Product;
import com.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


/**
 * 商品管理控制器
 */
@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "分页查询商品列表")
    @GetMapping("/list")
    public R<IPage<Product>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) Long categoryId) {
        IPage<Product> page = productService.listPage(pageNum, pageSize, keyword, categoryId);
        return R.ok(page);
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public R<Product> getDetail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return R.fail("商品不存在");
        }
        return R.ok(product);
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public R<?> add(@RequestBody @Valid ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return R.ok();
    }

    @Operation(summary = "修改商品")
    @PutMapping
    public R<?> update(@RequestBody @Valid ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return R.ok();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return R.ok();
    }

    @Operation(summary = "修改商品状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return R.ok();
    }
}
