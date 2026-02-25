package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pages")
public class Page {
    @TableId
    private String id;  // UUID主键

    private String siteId;
    private String pageName;
    private String pageSlug;
    private String pageDescription;
    private Integer pageStatus;
    private Integer pageSort;
    private String pageMetaTitle;
    private String pageMetaDescription;
    private String pageMetaKeywords;
    private LocalDateTime publishedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
