package com.demo.controller;

import com.demo.common.R;
import com.demo.dto.LoginDTO;
import com.demo.dto.RegisterDTO;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        // 查询用户
        User user = userService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            return R.fail("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return R.fail("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            return R.fail("用户已被禁用");
        }

        // 生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        user.setPassword(null);
        result.put("userInfo", user);

        return R.ok(result);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<?> register(@RequestBody @Valid RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = userService.getByUsername(registerDTO.getUsername());
        if (existUser != null) {
            return R.fail("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1);
        user.setRoleId(2L); // 默认普通用户角色
        userService.save(user);

        return R.ok();
    }

    @Operation(summary = "用户退出")
    @GetMapping("/logout")
    public R<?> logout() {
        // JWT是无状态的，前端删除Token即可
        return R.ok();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public R<?> getUserInfo() {
        // 从SecurityContext获取用户ID
        Object principal = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            return R.fail("未登录");
        }

        Long userId = Long.valueOf(principal.toString());
        User user = userService.getById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }
        // 清空密码
        user.setPassword(null);
        return R.ok(user);
    }
}
