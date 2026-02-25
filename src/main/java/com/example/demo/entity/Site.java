package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sites")
public class Site {
    @TableId
    private String id;  // UUID主键

    private String siteName;
    private String siteDomain;
    private String siteDescription;
    private String logoImageUrl;
    private String logoAltText;
    private String navCtaText;
    private String navCtaLink;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Integer siteStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
}
