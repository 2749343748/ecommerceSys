package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Address;

import java.util.List;

/**
 * 收货地址服务接口
 */
public interface AddressService extends IService<Address> {

    /**
     * 查询用户的收货地址列表
     */
    List<Address> listByUserId(Long userId);

    /**
     * 新增收货地址
     */
    void addAddress(Address address);

    /**
     * 修改收货地址
     */
    void updateAddress(Address address);

    /**
     * 删除收货地址
     */
    void deleteAddress(Long id);

    /**
     * 设置默认地址
     */
    void setDefault(Long id, Long userId);
}
