package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.SpecificationValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 规格值Mapper接口
 */
@Mapper
public interface SpecificationValueMapper extends BaseMapper<SpecificationValue> {

    /**
     * 根据规格ID查询规格值列表
     */
    @Select("SELECT * FROM specification_value WHERE spec_id = #{specId} ORDER BY sort")
    List<SpecificationValue> selectBySpecId(@Param("specId") Long specId);
}
