package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.BlockType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlockTypeMapper extends BaseMapper<BlockType> {

    /**
     * 根据类型代码查询区块类型
     */
    @Select("SELECT * FROM block_types WHERE type_code = #{typeCode} AND is_active = 1")
    BlockType selectByTypeCode(String typeCode);
}
