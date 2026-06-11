package com.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@Data
@TableName("cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否选中：0否 1是
     */
    private Integer checked;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 商品名称（非数据库字段）
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 商品价格（非数据库字段）
     */
    @TableField(exist = false)
    private java.math.BigDecimal productPrice;

    /**
     * 商品主图（非数据库字段）
     */
    @TableField(exist = false)
    private String productImage;

    /**
     * 商品库存（非数据库字段）
     */
    @TableField(exist = false)
    private Integer productStock;
}
