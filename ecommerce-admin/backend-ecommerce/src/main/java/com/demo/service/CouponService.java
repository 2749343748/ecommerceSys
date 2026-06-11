package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Coupon;

/**
 * 优惠券服务接口
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 分页查询优惠券列表
     */
    IPage<Coupon> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 新增优惠券
     */
    void addCoupon(Coupon coupon);

    /**
     * 修改优惠券
     */
    void updateCoupon(Coupon coupon);

    /**
     * 删除优惠券
     */
    void deleteCoupon(Long id);

    /**
     * 修改优惠券状态
     */
    void updateStatus(Long id, Integer status);
}
