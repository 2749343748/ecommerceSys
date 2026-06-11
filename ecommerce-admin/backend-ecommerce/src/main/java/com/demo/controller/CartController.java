package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Cart;
import com.demo.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 */
@Tag(name = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "查询用户购物车列表")
    @GetMapping("/list")
    public R<List<Cart>> list(@RequestParam Long userId) {
        List<Cart> list = cartService.listByUserId(userId);
        return R.ok(list);
    }

    /**
     * 从SecurityContext获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(principal.toString());
    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public R<?> add(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer quantity) {
        Long userId = getCurrentUserId();
        cartService.addToCart(userId, productId, quantity);
        return R.ok();
    }

    @Operation(summary = "更新购物车数量")
    @PutMapping("/quantity")
    public R<?> updateQuantity(@RequestParam Long id, @RequestParam Integer quantity) {
        cartService.updateQuantity(id, quantity);
        return R.ok();
    }

    @Operation(summary = "更新选中状态")
    @PutMapping("/checked")
    public R<?> updateChecked(@RequestParam Long id, @RequestParam Integer checked) {
        cartService.updateChecked(id, checked);
        return R.ok();
    }

    @Operation(summary = "全选/取消全选")
    @PutMapping("/checkAll")
    public R<?> checkAll(@RequestParam Integer checked) {
        Long userId = getCurrentUserId();
        cartService.checkAll(userId, checked);
        return R.ok();
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        cartService.deleteCartItem(id);
        return R.ok();
    }
}
