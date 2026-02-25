package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Block;
import com.example.demo.mapper.BlockMapper;
import com.example.demo.service.BlockService;
import com.example.demo.dto.BlockContentDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlockServiceImpl extends ServiceImpl<BlockMapper, Block> implements BlockService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlockMapper blockMapper;

    @Override
    public List<Block> getBlocksByPageId(String pageId) {
        return blockMapper.selectByPageId(pageId);
    }

    @Override
    public List<Block> getBlocksByPageSlug(String siteId, String pageSlug) {
        return blockMapper.selectByPageSlug(pageSlug, siteId);
    }

    @Override
    public Map<String, Object> getBlockContent(String blockId) {
        Map<String, Object> result = blockMapper.selectBlockContent(blockId);
        if (result != null && result.get("block_content") != null) {
            try {
                String contentJson = (String) result.get("block_content");
                Map<String, Object> content = objectMapper.readValue(contentJson,
                    new TypeReference<Map<String, Object>>() {});
                result.putAll(content);
                result.remove("block_content");
            } catch (Exception e) {
                log.error("解析区块内容JSON失败", e);
            }
        }
        return result;
    }

    @Override
    public BlockContentDTO getBlockContentDTO(String blockId) {
        Block block = this.getById(blockId);
        if (block == null) {
            return null;
        }

        BlockContentDTO dto = new BlockContentDTO();
        dto.setBlockId(block.getId());
        dto.setBlockType(block.getBlockType());
        dto.setBlockName(block.getBlockName());

        if (block.getBlockContent() != null && !block.getBlockContent().isBlank()) {
            try {
                Map<String, Object> content = objectMapper.readValue(block.getBlockContent(),
                    new TypeReference<Map<String, Object>>() {});
                dto.setContent(content != null ? content : Collections.emptyMap());
            } catch (Exception e) {
                log.error("解析区块内容JSON失败", e);
                dto.setContent(Collections.emptyMap());
            }
        } else {
            dto.setContent(Collections.emptyMap());
        }

        return dto;
    }

    @Override
    public List<BlockContentDTO> getPageBlocks(String pageId) {
        List<Block> blocks = this.getBlocksByPageId(pageId);
        if (blocks.isEmpty()) {
            return new ArrayList<>();
        }

        return blocks.stream()
            .filter(block -> block != null && block.getBlockType() != null && !block.getBlockType().isBlank())
            .map(block -> {
                BlockContentDTO dto = new BlockContentDTO();
                dto.setBlockId(block.getId());
                dto.setBlockType(block.getBlockType());
                dto.setBlockName(block.getBlockName());
                dto.setBlockSort(block.getBlockSort());

                if (block.getBlockContent() != null && !block.getBlockContent().isBlank()) {
                    try {
                        Map<String, Object> content = objectMapper.readValue(block.getBlockContent(),
                            new TypeReference<Map<String, Object>>() {});
                        dto.setContent(content != null ? content : Collections.emptyMap());
                    } catch (Exception e) {
                        log.error("解析区块内容JSON失败", e);
                        dto.setContent(Collections.emptyMap());
                    }
                } else {
                    dto.setContent(Collections.emptyMap());
                }

                return dto;
            })
            .collect(Collectors.toList());
    }
}
