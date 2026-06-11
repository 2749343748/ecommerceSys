package com.demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功返回
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /**
     * 成功返回（自定义消息）
     */
    public static <T> R<T> ok(String msg, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 失败返回
     */
    public static <T> R<T> fail() {
        return fail("操作失败");
    }

    /**
     * 失败返回（自定义消息）
     */
    public static <T> R<T> fail(String msg) {
        return fail(500, msg);
    }

    /**
     * 失败返回（自定义状态码和消息）
     */
    public static <T> R<T> fail(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
