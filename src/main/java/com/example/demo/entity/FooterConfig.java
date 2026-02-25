package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("footer_config")
public class FooterConfig {
    @TableId
    private String id;

    private String siteId;
    private String copyright;
    private String companyName;
    private String logoImageUrl;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
