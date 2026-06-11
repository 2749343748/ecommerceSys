package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Promotion;

/**
 * 促销活动服务接口
 */
public interface PromotionService extends IService<Promotion> {

    /**
     * 分页查询促销活动列表
     */
    IPage<Promotion> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 新增促销活动
     */
    void addPromotion(Promotion promotion);

    /**
     * 修改促销活动
     */
    void updatePromotion(Promotion promotion);

    /**
     * 删除促销活动
     */
    void deletePromotion(Long id);

    /**
     * 修改活动状态
     */
    void updateStatus(Long id, Integer status);
}
