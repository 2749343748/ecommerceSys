package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Review;

/**
 * 评价服务接口
 */
public interface ReviewService extends IService<Review> {

    /**
     * 分页查询评价列表
     */
    IPage<Review> listPage(Integer pageNum, Integer pageSize, Long productId, Integer status);

    /**
     * 回复评价
     */
    void reply(Long id, String reply);

    /**
     * 修改评价状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除评价
     */
    void deleteReview(Long id);
}
