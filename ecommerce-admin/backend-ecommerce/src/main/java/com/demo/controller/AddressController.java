package com.demo.controller;

import com.demo.common.R;
import com.demo.entity.Address;
import com.demo.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址控制器
 */
@Tag(name = "收货地址")
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Operation(summary = "查询用户收货地址列表")
    @GetMapping("/list")
    public R<List<Address>> list(@RequestParam Long userId) {
        List<Address> list = addressService.listByUserId(userId);
        return R.ok(list);
    }

    @Operation(summary = "新增收货地址")
    @PostMapping
    public R<?> add(@RequestBody Address address) {
        addressService.addAddress(address);
        return R.ok();
    }

    @Operation(summary = "修改收货地址")
    @PutMapping
    public R<?> update(@RequestBody Address address) {
        addressService.updateAddress(address);
        return R.ok();
    }

    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return R.ok();
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/default")
    public R<?> setDefault(@RequestParam Long id, @RequestParam Long userId) {
        addressService.setDefault(id, userId);
        return R.ok();
    }
}
