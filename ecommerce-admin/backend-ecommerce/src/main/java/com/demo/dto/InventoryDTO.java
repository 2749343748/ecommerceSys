package com.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存请求参数
 */
@Data
public class InventoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;
}
