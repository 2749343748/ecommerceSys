package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Specification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 规格Mapper接口
 */
@Mapper
public interface SpecificationMapper extends BaseMapper<Specification> {

    /**
     * 根据分类ID查询规格列表
     */
    @Select("SELECT * FROM specification WHERE category_id = #{categoryId} AND deleted = 0 ORDER BY sort")
    List<Specification> selectByCategoryId(@Param("categoryId") Long categoryId);
}
