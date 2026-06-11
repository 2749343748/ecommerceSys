package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Inventory;
import com.demo.entity.InventoryRecord;

/**
 * 库存服务接口
 */
public interface InventoryService extends IService<Inventory> {

    /**
     * 分页查询库存列表
     */
    IPage<Inventory> listPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 入库
     */
    void stockIn(Long productId, Integer quantity, String remark);

    /**
     * 出库
     */
    void stockOut(Long productId, Integer quantity, String remark);

    /**
     * 分页查询库存记录
     */
    IPage<InventoryRecord> listRecords(Integer pageNum, Integer pageSize, Long productId);
}
