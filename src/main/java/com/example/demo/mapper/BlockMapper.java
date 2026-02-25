package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Block;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockMapper extends BaseMapper<Block> {

    /**
     * 根据页面ID查询该页面的所有区块，按排序字段升序
     */
    @Select("SELECT * FROM blocks WHERE page_id = #{pageId} AND block_status = 1 ORDER BY block_sort ASC")
    List<Block> selectByPageId(String pageId);

    /**
     * 根据页面路径查询区块
     */
    @Select("SELECT b.* FROM blocks b " +
            "INNER JOIN pages p ON b.page_id = p.id " +
            "WHERE p.page_slug = #{pageSlug} AND p.site_id = #{siteId} " +
            "AND b.block_status = 1 ORDER BY b.block_sort ASC")
    List<Block> selectByPageSlug(String pageSlug, String siteId);

    /**
     * 查询区块内容并转换为Map
     */
    @Select("SELECT id, block_type, block_content FROM blocks WHERE id = #{blockId}")
    Map<String, Object> selectBlockContent(String blockId);
}
