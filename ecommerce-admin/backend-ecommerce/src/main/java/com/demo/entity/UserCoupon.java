package com.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 */
@Data
@TableName("user_coupon")
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 状态：0未使用 1已使用 2已过期
     */
    private Integer status;

    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 优惠券名称（非数据库字段）
     */
    @TableField(exist = false)
    private String couponName;

    /**
     * 优惠券类型（非数据库字段）
     */
    @TableField(exist = false)
    private Integer couponType;

    /**
     * 优惠值（非数据库字段）
     */
    @TableField(exist = false)
    private java.math.BigDecimal couponValue;
}
