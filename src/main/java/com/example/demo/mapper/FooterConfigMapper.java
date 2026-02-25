package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.FooterConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FooterConfigMapper extends BaseMapper<FooterConfig> {

    @Select("SELECT * FROM footer_config WHERE site_id = #{siteId} AND is_active = 1 LIMIT 1")
    FooterConfig selectBySiteId(String siteId);
}
