package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageBlocksDTO {
    private String pageId;
    private String pageName;
    private String pageSlug;
    private List<BlockContentDTO> blocks;
}
