package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("block_types")
public class BlockType {
    @TableId
    private Integer id;  // 自增主键

    private String typeCode;
    private String typeName;
    private String typeDescription;
    private String templateFile;
    private String contentSchema;  // JSON字符串
    private String category;
    private Integer sort;
    private Integer isActive;
    private LocalDateTime createdAt;
}
