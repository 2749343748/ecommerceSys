package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.BusinessException;
import com.demo.entity.Inventory;
import com.demo.entity.InventoryRecord;
import com.demo.entity.Product;
import com.demo.mapper.InventoryMapper;
import com.demo.mapper.InventoryRecordMapper;
import com.demo.mapper.ProductMapper;
import com.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 库存服务实现类
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryRecordMapper recordMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public IPage<Inventory> listPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Inventory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        // 可以通过商品名称搜索，需要关联查询
        wrapper.orderByDesc(Inventory::getCreateTime);
        IPage<Inventory> result = inventoryMapper.selectPage(page, wrapper);
        // 填充商品信息
        result.getRecords().forEach(inv -> {
            Product product = productMapper.selectById(inv.getProductId());
            if (product != null) {
                inv.setProductName(product.getName());
                inv.setProductImage(product.getMainImage());
            }
        });
        return result;
    }

    @Override
    @Transactional
    public void stockIn(Long productId, Integer quantity, String remark) {
        Inventory inventory = inventoryMapper.selectByProductId(productId);
        if (inventory == null) {
            // 创建库存记录
            inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setWarehouse("默认仓库");
            inventory.setQuantity(quantity);
            inventory.setWarningQuantity(10);
            inventoryMapper.insert(inventory);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryMapper.updateById(inventory);
        }
        // 记录入库日志
        InventoryRecord record = new InventoryRecord();
        record.setProductId(productId);
        record.setType(1);
        record.setQuantity(quantity);
        record.setRemark(remark);
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void stockOut(Long productId, Integer quantity, String remark) {
        Inventory inventory = inventoryMapper.selectByProductId(productId);
        if (inventory == null || inventory.getQuantity() < quantity) {
            throw new BusinessException("库存不足");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryMapper.updateById(inventory);
        // 记录出库日志
        InventoryRecord record = new InventoryRecord();
        record.setProductId(productId);
        record.setType(2);
        record.setQuantity(quantity);
        record.setRemark(remark);
        recordMapper.insert(record);
    }

    @Override
    public IPage<InventoryRecord> listRecords(Integer pageNum, Integer pageSize, Long productId) {
        Page<InventoryRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<InventoryRecord> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(InventoryRecord::getProductId, productId);
        }
        wrapper.orderByDesc(InventoryRecord::getCreateTime);
        return recordMapper.selectPage(page, wrapper);
    }
}
