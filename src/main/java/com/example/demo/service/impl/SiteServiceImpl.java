package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Site;
import com.example.demo.mapper.SiteMapper;
import com.example.demo.service.SiteService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements SiteService {

    @Override
    public Site getCurrentSite() {
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getSiteStatus, 1)
               .last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public Site getSiteByDomain(String domain) {
        if (!StringUtils.hasText(domain)) {
            return null;
        }
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getSiteDomain, domain)
               .eq(Site::getSiteStatus, 1);
        return this.getOne(wrapper);
    }
}
