package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Promotion;
import com.demo.mapper.PromotionMapper;
import com.demo.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 促销活动服务实现类
 */
@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion> implements PromotionService {

    @Autowired
    private PromotionMapper promotionMapper;

    @Override
    public IPage<Promotion> listPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Promotion> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Promotion> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Promotion::getName, keyword);
        }
        wrapper.orderByDesc(Promotion::getCreateTime);
        return promotionMapper.selectPage(page, wrapper);
    }

    @Override
    public void addPromotion(Promotion promotion) {
        promotionMapper.insert(promotion);
    }

    @Override
    public void updatePromotion(Promotion promotion) {
        promotionMapper.updateById(promotion);
    }

    @Override
    public void deletePromotion(Long id) {
        promotionMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Promotion promotion = promotionMapper.selectById(id);
        if (promotion == null) {
            throw new BusinessException("促销活动不存在");
        }
        promotion.setStatus(status);
        promotionMapper.updateById(promotion);
    }
}
