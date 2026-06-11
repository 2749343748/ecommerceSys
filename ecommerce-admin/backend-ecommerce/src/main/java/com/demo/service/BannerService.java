package com.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Banner;

/**
 * 轮播图服务接口
 */
public interface BannerService extends IService<Banner> {

    /**
     * 分页查询轮播图列表
     */
    IPage<Banner> listPage(Integer pageNum, Integer pageSize);

    /**
     * 新增轮播图
     */
    void addBanner(Banner banner);

    /**
     * 修改轮播图
     */
    void updateBanner(Banner banner);

    /**
     * 删除轮播图
     */
    void deleteBanner(Long id);

    /**
     * 修改轮播图状态
     */
    void updateStatus(Long id, Integer status);
}
