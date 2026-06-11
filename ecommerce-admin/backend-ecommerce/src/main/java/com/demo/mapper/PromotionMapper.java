package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Promotion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 促销活动Mapper接口
 */
@Mapper
public interface PromotionMapper extends BaseMapper<Promotion> {
}
