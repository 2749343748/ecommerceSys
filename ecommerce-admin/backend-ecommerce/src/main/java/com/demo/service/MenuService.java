package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询菜单树
     */
    List<Menu> getMenuTree();

    /**
     * 新增菜单
     */
    void addMenu(Menu menu);

    /**
     * 修改菜单
     */
    void updateMenu(Menu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
}
