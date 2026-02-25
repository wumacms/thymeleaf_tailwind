package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.FooterDTO;
import com.example.demo.entity.FooterConfig;
import com.example.demo.mapper.FooterConfigMapper;
import com.example.demo.service.FooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FooterServiceImpl extends ServiceImpl<FooterConfigMapper, FooterConfig> implements FooterService {

    @Autowired
    private FooterConfigMapper footerConfigMapper;

    @Override
    public FooterDTO getFooterBySiteId(String siteId) {
        FooterConfig config = footerConfigMapper.selectBySiteId(siteId);
        if (config == null) {
            return null;
        }
        FooterDTO dto = new FooterDTO();
        dto.setCompanyName(config.getCompanyName());
        dto.setLogoImageUrl(config.getLogoImageUrl());
        dto.setCopyright(config.getCopyright());
        return dto;
    }
}
