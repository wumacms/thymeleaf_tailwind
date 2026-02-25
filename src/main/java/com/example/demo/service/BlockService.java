package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Block;
import com.example.demo.dto.BlockContentDTO;

import java.util.List;
import java.util.Map;

public interface BlockService extends IService<Block> {
    /**
     * 根据页面ID获取所有区块
     */
    List<Block> getBlocksByPageId(String pageId);

    /**
     * 根据页面路径获取所有区块
     */
    List<Block> getBlocksByPageSlug(String siteId, String pageSlug);

    /**
     * 获取区块内容（解析JSON）
     */
    Map<String, Object> getBlockContent(String blockId);

    /**
     * 获取区块内容DTO（包含区块类型和内容）
     */
    BlockContentDTO getBlockContentDTO(String blockId);

    /**
     * 批量获取页面所有区块的内容
     */
    List<BlockContentDTO> getPageBlocks(String pageId);
}
