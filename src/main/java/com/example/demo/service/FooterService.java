package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.FooterDTO;
import com.example.demo.entity.FooterConfig;

public interface FooterService extends IService<FooterConfig> {

    /**
     * 根据站点ID获取页脚数据
     */
    FooterDTO getFooterBySiteId(String siteId);
}
