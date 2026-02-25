package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Site;

public interface SiteService extends IService<Site> {
    /**
     * 获取当前站点信息（通常只有一个站点）
     */
    Site getCurrentSite();

    /**
     * 根据域名获取站点
     */
    Site getSiteByDomain(String domain);
}
