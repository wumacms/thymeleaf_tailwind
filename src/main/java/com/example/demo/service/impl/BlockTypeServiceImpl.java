package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.BlockType;
import com.example.demo.mapper.BlockTypeMapper;
import com.example.demo.service.BlockTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class BlockTypeServiceImpl extends ServiceImpl<BlockTypeMapper, BlockType> implements BlockTypeService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public BlockType getByTypeCode(String typeCode) {
        LambdaQueryWrapper<BlockType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlockType::getTypeCode, typeCode)
               .eq(BlockType::getIsActive, 1);
        return this.getOne(wrapper);
    }

    @Override
    public boolean validateBlockContent(String typeCode, String content) {
        BlockType blockType = this.getByTypeCode(typeCode);
        if (blockType == null || blockType.getContentSchema() == null) {
            return true; // 没有定义Schema则跳过验证
        }

        try {
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema schema = factory.getSchema(blockType.getContentSchema());

            Set<ValidationMessage> errors = schema.validate(objectMapper.readTree(content));
            return errors.isEmpty();
        } catch (Exception e) {
            log.error("验证区块内容失败", e);
            return false;
        }
    }
}
