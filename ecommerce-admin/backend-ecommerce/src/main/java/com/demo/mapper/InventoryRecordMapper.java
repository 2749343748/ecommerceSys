package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.InventoryRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存记录Mapper接口
 */
@Mapper
public interface InventoryRecordMapper extends BaseMapper<InventoryRecord> {
}
