package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取所有角色列表
     */
    List<Role> listAll();
}
