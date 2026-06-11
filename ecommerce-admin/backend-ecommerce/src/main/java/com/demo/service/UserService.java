package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.dto.UserDTO;
import com.demo.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户
     */
    IPage<User> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 新增用户
     */
    void addUser(UserDTO userDTO);

    /**
     * 修改用户
     */
    void updateUser(UserDTO userDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 重置密码
     */
    void resetPassword(Long id);

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);
}
