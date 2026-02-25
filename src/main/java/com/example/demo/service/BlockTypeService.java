package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.BlockType;

public interface BlockTypeService extends IService<BlockType> {
    /**
     * 根据类型代码获取区块类型
     */
    BlockType getByTypeCode(String typeCode);

    /**
     * 验证区块内容是否符合Schema
     */
    boolean validateBlockContent(String typeCode, String content);
}
