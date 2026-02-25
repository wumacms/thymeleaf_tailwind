package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Page;
import com.example.demo.mapper.PageMapper;
import com.example.demo.service.PageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Override
    public List<Page> getPagesBySiteId(String siteId) {
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Page::getSiteId, siteId)
               .eq(Page::getPageStatus, 1)
               .orderByAsc(Page::getPageSort);
        return this.list(wrapper);
    }

    @Override
    public Page getPageBySlug(String siteId, String pageSlug) {
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Page::getSiteId, siteId)
               .eq(Page::getPageSlug, pageSlug)
               .eq(Page::getPageStatus, 1);
        return this.getOne(wrapper);
    }

    @Override
    public Page getHomePage(String siteId) {
        return getPageBySlug(siteId, "/");
    }
}
