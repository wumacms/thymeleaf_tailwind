package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blocks")
public class Block {
    @TableId
    private String id;  // UUID主键

    private String siteId;
    private String pageId;
    private String blockType;
    private String blockName;
    private String blockDescription;
    private String blockContent;  // JSON字符串
    private Integer blockStatus;
    private Integer blockSort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
}
