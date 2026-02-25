package com.example.demo.dto;

import lombok.Data;
import java.util.Map;

@Data
public class BlockContentDTO {
    private String blockId;
    private String blockType;
    private String blockName;
    private Integer blockSort;
    private Map<String, Object> content;
}
