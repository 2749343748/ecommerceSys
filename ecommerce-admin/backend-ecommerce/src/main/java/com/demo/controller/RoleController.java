package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Role;
import com.demo.mapper.RoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @Operation(summary = "查询角色列表")
    @GetMapping("/list")
    public R<List<Role>> list() {
        List<Role> list = roleMapper.selectList(null);
        return R.ok(list);
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public R<?> add(@RequestBody Role role) {
        roleMapper.insert(role);
        return R.ok();
    }

    @Operation(summary = "修改角色")
    @PutMapping
    public R<?> update(@RequestBody Role role) {
        roleMapper.updateById(role);
        return R.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        roleMapper.deleteById(id);
        return R.ok();
    }

    @Operation(summary = "修改角色状态")
    @PutMapping("/status")
    public R<?> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        Role role = roleMapper.selectById(id);
        if (role != null) {
            role.setStatus(status);
            roleMapper.updateById(role);
        }
        return R.ok();
    }
}
