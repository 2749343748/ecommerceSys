package com.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 品牌请求参数
 */
@Data
public class BrandDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌ID（修改时必填）
     */
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌Logo
     */
    private String logo;

    /**
     * 品牌描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;
}
