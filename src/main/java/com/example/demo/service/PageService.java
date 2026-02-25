package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Page;

import java.util.List;

public interface PageService extends IService<Page> {
    /**
     * 根据站点ID查询所有页面
     */
    List<Page> getPagesBySiteId(String siteId);

    /**
     * 根据页面路径查询页面
     */
    Page getPageBySlug(String siteId, String pageSlug);

    /**
     * 获取首页
     */
    Page getHomePage(String siteId);
}
