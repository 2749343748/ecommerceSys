package com.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存记录实体类
 */
@Data
@TableName("inventory_record")
public class InventoryRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 类型：1入库 2出库
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人ID
     */
    private Long operatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 商品名称（非数据库字段）
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 操作人名称（非数据库字段）
     */
    @TableField(exist = false)
    private String operatorName;
}
