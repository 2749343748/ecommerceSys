package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌Mapper接口
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {
}
