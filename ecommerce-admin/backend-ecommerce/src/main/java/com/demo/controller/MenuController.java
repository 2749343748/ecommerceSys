package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Menu;
import com.demo.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Operation(summary = "查询菜单树")
    @GetMapping("/tree")
    public R<List<Menu>> tree() {
        List<Menu> tree = menuService.getMenuTree();
        return R.ok(tree);
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    public R<?> add(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return R.ok();
    }

    @Operation(summary = "修改菜单")
    @PutMapping
    public R<?> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return R.ok();
    }
}
