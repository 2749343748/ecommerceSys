package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 库存Mapper接口
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 根据商品ID查询库存
     */
    @Select("SELECT * FROM inventory WHERE product_id = #{productId} LIMIT 1")
    Inventory selectByProductId(@Param("productId") Long productId);
}
