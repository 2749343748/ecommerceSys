package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流Mapper接口
 */
@Mapper
public interface LogisticsMapper extends BaseMapper<Logistics> {
}
