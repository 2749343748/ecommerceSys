package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.entity.Review;
import com.demo.entity.User;
import com.demo.mapper.ReviewMapper;
import com.demo.mapper.UserMapper;
import com.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评价服务实现类
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<Review> listPage(Integer pageNum, Integer pageSize, Long productId, Integer status) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(Review::getProductId, productId);
        }
        if (status != null) {
            wrapper.eq(Review::getStatus, status);
        }
        wrapper.orderByDesc(Review::getCreateTime);
        IPage<Review> result = reviewMapper.selectPage(page, wrapper);
        // 填充用户名
        result.getRecords().forEach(review -> {
            User user = userMapper.selectById(review.getUserId());
            if (user != null) {
                review.setUsername(user.getUsername());
            }
        });
        return result;
    }

    @Override
    public void reply(Long id, String reply) {
        Review review = reviewMapper.selectById(id);
        review.setReply(reply);
        reviewMapper.updateById(review);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Review review = reviewMapper.selectById(id);
        review.setStatus(status);
        reviewMapper.updateById(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewMapper.deleteById(id);
    }
}
