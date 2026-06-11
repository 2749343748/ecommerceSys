package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Category;
import com.demo.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类控制器
 */
@Tag(name = "商品分类")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    @Operation(summary = "获取分类树")
    @GetMapping("/tree")
    public R<List<Category>> tree() {
        List<Category> allCategories = categoryMapper.selectList(null);
        List<Category> tree = buildTree(allCategories, 0L);
        return R.ok(tree);
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public R<?> add(@RequestBody Category category) {
        if (category.getParentId() == null || category.getParentId() == 0) {
            category.setLevel(1);
        } else {
            Category parent = categoryMapper.selectById(category.getParentId());
            category.setLevel(parent != null ? parent.getLevel() + 1 : 1);
        }
        categoryMapper.insert(category);
        return R.ok();
    }

    @Operation(summary = "修改分类")
    @PutMapping
    public R<?> update(@RequestBody Category category) {
        categoryMapper.updateById(category);
        return R.ok();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        // 检查是否有子分类
        var wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Category>();
        wrapper.eq(Category::getParentId, id);
        if (categoryMapper.selectCount(wrapper) > 0) {
            return R.fail("存在子分类，无法删除");
        }
        categoryMapper.deleteById(id);
        return R.ok();
    }

    /**
     * 构建分类树
     */
    private List<Category> buildTree(List<Category> categories, Long parentId) {
        return categories.stream()
                .filter(cat -> parentId.equals(cat.getParentId()))
                .peek(cat -> cat.setChildren(buildTree(categories, cat.getId())))
                .collect(Collectors.toList());
    }
}
