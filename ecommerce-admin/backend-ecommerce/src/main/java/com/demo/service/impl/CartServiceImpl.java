package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Cart;
import com.demo.entity.Product;
import com.demo.mapper.CartMapper;
import com.demo.mapper.ProductMapper;
import com.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Cart> listByUserId(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getCreateTime);
        List<Cart> cartList = cartMapper.selectList(wrapper);
        // 填充商品信息
        cartList.forEach(cart -> {
            Product product = productMapper.selectById(cart.getProductId());
            if (product != null) {
                cart.setProductName(product.getName());
                cart.setProductPrice(product.getPrice());
                cart.setProductImage(product.getMainImage());
                cart.setProductStock(product.getStock());
            }
        });
        return cartList;
    }

    @Override
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException("商品不存在或已下架");
        }
        // 检查购物车中是否已有该商品
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId);
        Cart existCart = cartMapper.selectOne(wrapper);
        if (existCart != null) {
            // 更新数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            cartMapper.updateById(existCart);
        } else {
            // 新增购物车
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setChecked(1);
            cartMapper.insert(cart);
        }
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException("购物车商品不存在");
        }
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
    }

    @Override
    public void updateChecked(Long id, Integer checked) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException("购物车商品不存在");
        }
        cart.setChecked(checked);
        cartMapper.updateById(cart);
    }

    @Override
    public void checkAll(Long userId, Integer checked) {
        Cart update = new Cart();
        update.setChecked(checked);
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        cartMapper.update(update, wrapper);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartMapper.deleteById(id);
    }
}
