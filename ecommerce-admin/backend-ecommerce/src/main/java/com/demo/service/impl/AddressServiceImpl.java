package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Address;
import com.demo.mapper.AddressMapper;
import com.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址服务实现类
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> listByUserId(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        return addressMapper.selectList(wrapper);
    }

    @Override
    public void addAddress(Address address) {
        // 如果是默认地址，取消其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getUserId());
        }
        addressMapper.insert(address);
    }

    @Override
    public void updateAddress(Address address) {
        // 如果是默认地址，取消其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getUserId());
        }
        addressMapper.updateById(address);
    }

    @Override
    public void deleteAddress(Long id) {
        addressMapper.deleteById(id);
    }

    @Override
    public void setDefault(Long id, Long userId) {
        // 清除该用户所有默认地址
        clearDefault(userId);
        // 设置当前地址为默认
        Address address = addressMapper.selectById(id);
        if (address != null) {
            address.setIsDefault(1);
            addressMapper.updateById(address);
        }
    }

    /**
     * 清除用户所有默认地址
     */
    private void clearDefault(Long userId) {
        Address update = new Address();
        update.setIsDefault(0);
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, 1);
        addressMapper.update(update, wrapper);
    }
}
