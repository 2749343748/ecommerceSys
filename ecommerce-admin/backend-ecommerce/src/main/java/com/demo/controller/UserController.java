package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.R;
import com.demo.dto.UserDTO;
import com.demo.entity.User;
import com.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/list")
    public R<IPage<User>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String keyword) {
        IPage<User> page = userService.listPage(pageNum, pageSize, keyword);
        // 清空密码
        page.getRecords().forEach(user -> user.setPassword(null));
        return R.ok(page);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<?> add(@RequestBody @Valid UserDTO userDTO) {
        userService.addUser(userDTO);
        return R.ok();
    }

    @Operation(summary = "修改用户")
    @PutMapping
    public R<?> update(@RequestBody @Valid UserDTO userDTO) {
        userService.updateUser(userDTO);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/resetPwd")
    public R<?> resetPassword(@RequestParam Long id) {
        userService.resetPassword(id);
        return R.ok();
    }
}
