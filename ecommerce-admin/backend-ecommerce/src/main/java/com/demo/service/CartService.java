package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService extends IService<Cart> {

    /**
     * 查询用户的购物车列表
     */
    List<Cart> listByUserId(Long userId);

    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车数量
     */
    void updateQuantity(Long id, Integer quantity);

    /**
     * 更新选中状态
     */
    void updateChecked(Long id, Integer checked);

    /**
     * 全选/取消全选
     */
    void checkAll(Long userId, Integer checked);

    /**
     * 删除购物车商品
     */
    void deleteCartItem(Long id);
}
