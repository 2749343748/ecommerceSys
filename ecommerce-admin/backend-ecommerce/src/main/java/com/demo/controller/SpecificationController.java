package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Specification;
import com.demo.entity.SpecificationValue;
import com.demo.service.SpecificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规格管理控制器
 */
@Tag(name = "规格管理")
@RestController
@RequestMapping("/api/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specService;

    @Operation(summary = "查询所有规格列表")
    @GetMapping("/all")
    public R<List<Specification>> listAll() {
        List<Specification> list = specService.list();
        return R.ok(list);
    }

    @Operation(summary = "根据分类ID查询规格列表")
    @GetMapping("/list")
    public R<List<Specification>> list(@RequestParam Long categoryId) {
        List<Specification> list = specService.listByCategoryId(categoryId);
        return R.ok(list);
    }

    @Operation(summary = "查询规格值列表")
    @GetMapping("/values")
    public R<List<SpecificationValue>> listValues(@RequestParam Long specId) {
        List<SpecificationValue> list = specService.listValuesBySpecId(specId);
        return R.ok(list);
    }

    @Operation(summary = "新增规格")
    @PostMapping
    public R<?> add(@RequestBody Specification specification) {
        specService.addSpecification(specification);
        return R.ok();
    }

    @Operation(summary = "修改规格")
    @PutMapping
    public R<?> update(@RequestBody Specification specification) {
        specService.updateSpecification(specification);
        return R.ok();
    }

    @Operation(summary = "删除规格")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        specService.deleteSpecification(id);
        return R.ok();
    }

    @Operation(summary = "新增规格值")
    @PostMapping("/value")
    public R<?> addValue(@RequestBody SpecificationValue specValue) {
        specService.addSpecValue(specValue);
        return R.ok();
    }

    @Operation(summary = "删除规格值")
    @DeleteMapping("/value/{id}")
    public R<?> deleteValue(@PathVariable Long id) {
        specService.deleteSpecValue(id);
        return R.ok();
    }
}
