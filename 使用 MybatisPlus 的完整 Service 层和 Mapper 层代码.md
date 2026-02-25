# 使用 MybatisPlus 的完整 Service 层和 Mapper 层代码

## 1. 实体类 (Entity)

```java
package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sites")
public class Site {
    @TableId
    private String id;  // UUID主键
    
    private String siteName;
    private String siteDomain;
    private String siteDescription;
    private String logoImageUrl;
    private String logoAltText;
    private String navCtaText;
    private String navCtaLink;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Integer siteStatus;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
}
```

```java
package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pages")
public class Page {
    @TableId
    private String id;  // UUID主键
    
    private String siteId;
    private String pageName;
    private String pageSlug;
    private String pageDescription;
    private Integer pageStatus;
    private Integer pageSort;
    private String pageMetaTitle;
    private String pageMetaDescription;
    private String pageMetaKeywords;
    private LocalDateTime publishedAt;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
```

```java
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
```

```java
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
```

## 2. Mapper层

```java
package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Site;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteMapper extends BaseMapper<Site> {
}
```

```java
package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PageMapper extends BaseMapper<Page> {
}
```

```java
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
```

```java
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
```

## 3. Service层

### 3.1 基础Service接口

```java
package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Site;

public interface SiteService extends IService<Site> {
    /**
     * 获取当前站点信息（通常只有一个站点）
     */
    Site getCurrentSite();
    
    /**
     * 根据域名获取站点
     */
    Site getSiteByDomain(String domain);
}
```

```java
package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Page;
import java.util.List;

public interface PageService extends IService<Page> {
    /**
     * 根据站点ID查询所有页面
     */
    List<Page> getPagesBySiteId(String siteId);
    
    /**
     * 根据页面路径查询页面
     */
    Page getPageBySlug(String siteId, String pageSlug);
    
    /**
     * 获取首页
     */
    Page getHomePage(String siteId);
}
```

```java
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
```

```java
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
```

### 3.2 Service实现类

```java
package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Site;
import com.example.demo.mapper.SiteMapper;
import com.example.demo.service.SiteService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements SiteService {

    @Override
    public Site getCurrentSite() {
        // 获取第一个启用的站点
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getSiteStatus, 1)
               .last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public Site getSiteByDomain(String domain) {
        if (!StringUtils.hasText(domain)) {
            return null;
        }
        LambdaQueryWrapper<Site> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Site::getSiteDomain, domain)
               .eq(Site::getSiteStatus, 1);
        return this.getOne(wrapper);
    }
}
```

```java
package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Page;
import com.example.demo.mapper.PageMapper;
import com.example.demo.service.PageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Override
    public List<Page> getPagesBySiteId(String siteId) {
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Page::getSiteId, siteId)
               .eq(Page::getPageStatus, 1)
               .orderByAsc(Page::getPageSort);
        return this.list(wrapper);
    }

    @Override
    public Page getPageBySlug(String siteId, String pageSlug) {
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Page::getSiteId, siteId)
               .eq(Page::getPageSlug, pageSlug)
               .eq(Page::getPageStatus, 1);
        return this.getOne(wrapper);
    }

    @Override
    public Page getHomePage(String siteId) {
        return getPageBySlug(siteId, "/");
    }
}
```

```java
package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Block;
import com.example.demo.mapper.BlockMapper;
import com.example.demo.service.BlockService;
import com.example.demo.dto.BlockContentDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        
        try {
            Map<String, Object> content = objectMapper.readValue(block.getBlockContent(), 
                new TypeReference<Map<String, Object>>() {});
            dto.setContent(content);
        } catch (Exception e) {
            log.error("解析区块内容JSON失败", e);
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
            .map(block -> {
                BlockContentDTO dto = new BlockContentDTO();
                dto.setBlockId(block.getId());
                dto.setBlockType(block.getBlockType());
                dto.setBlockName(block.getBlockName());
                dto.setBlockSort(block.getBlockSort());
                
                try {
                    Map<String, Object> content = objectMapper.readValue(block.getBlockContent(), 
                        new TypeReference<Map<String, Object>>() {});
                    dto.setContent(content);
                } catch (Exception e) {
                    log.error("解析区块内容JSON失败", e);
                }
                
                return dto;
            })
            .collect(Collectors.toList());
    }
}
```

```java
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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
```

## 4. DTO类

```java
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
```

```java
package com.example.demo.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PageBlocksDTO {
    private String pageId;
    private String pageName;
    private String pageSlug;
    private List<BlockContentDTO> blocks;
}
```

## 5. 配置类

```java
package com.example.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis-Plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 自动填充处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
                
                // 获取当前用户名
                String username = getCurrentUsername();
                this.strictInsertFill(metaObject, "createdBy", String.class, username);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }
            
            private String getCurrentUsername() {
                try {
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (principal instanceof UserDetails) {
                        return ((UserDetails) principal).getUsername();
                    } else {
                        return principal.toString();
                    }
                } catch (Exception e) {
                    return "system";
                }
            }
        };
    }
}
```

## 6. 更新后的控制器

```java
package com.example.demo.controller;

import com.example.demo.entity.Page;
import com.example.demo.entity.Site;
import com.example.demo.service.BlockService;
import com.example.demo.service.PageService;
import com.example.demo.service.SiteService;
import com.example.demo.dto.BlockContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private SiteService siteService;
    
    @Autowired
    private PageService pageService;
    
    @Autowired
    private BlockService blockService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String home(Model model) {
        // 获取当前站点
        Site site = siteService.getCurrentSite();
        if (site == null) {
            return "error/404";
        }
        model.addAttribute("site", site);
        
        // 获取首页
        Page homePage = pageService.getHomePage(site.getId());
        if (homePage == null) {
            return "error/404";
        }
        
        // 获取首页的所有区块
        List<BlockContentDTO> blocks = blockService.getPageBlocks(homePage.getId());
        model.addAttribute("blocks", blocks);
        
        return "index";
    }
    
    /**
     * 自定义页面
     */
    @GetMapping("/{pageSlug}")
    public String page(@PathVariable String pageSlug, Model model) {
        // 获取当前站点
        Site site = siteService.getCurrentSite();
        if (site == null) {
            return "error/404";
        }
        model.addAttribute("site", site);
        
        // 获取页面
        Page page = pageService.getPageBySlug(site.getId(), "/" + pageSlug);
        if (page == null) {
            return "error/404";
        }
        
        // 获取页面的所有区块
        List<BlockContentDTO> blocks = blockService.getPageBlocks(page.getId());
        model.addAttribute("blocks", blocks);
        
        return "page";
    }
    
    /**
     * 渲染单个区块（用于AJAX加载）
     */
    @GetMapping("/block/{blockId}")
    public String renderBlock(@PathVariable String blockId, Model model) {
        BlockContentDTO blockContent = blockService.getBlockContentDTO(blockId);
        if (blockContent == null) {
            return "error/404";
        }
        
        model.addAttribute("blockContent", blockContent.getContent());
        return "fragments/sections/" + blockContent.getBlockType() + " :: " + blockContent.getBlockType();
    }
}
```

## 7. 更新后的主模板 (index.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${site.metaTitle}">ChatFlow - 企业级即时通讯平台</title>
    <meta name="description" th:content="${site.metaDescription}">
    <meta name="keywords" th:content="${site.metaKeywords}">
    <!-- Tailwind via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-white text-gray-800 antialiased">

    <!-- 导航栏 -->
    <div th:replace="~{fragments/header :: header(site=${site})}"></div>

    <main>
        <!-- 动态渲染所有区块 -->
        <div th:each="block : ${blocks}" 
             th:replace="~{fragments/sections/${block.blockType} :: ${block.blockType}(blockContent=${block.content})}">
        </div>
    </main>

    <!-- 页脚 -->
    <div th:replace="~{fragments/footer :: footer}"></div>

</body>
</html>
```

## 8. 更新后的导航栏模板 (header.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:remove="all">Header Fragment</title>
</head>
<body>
    <header th:fragment="header(site)" class="border-b border-gray-100 bg-white/80 backdrop-blur-sm sticky top-0 z-10">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
            <!-- Logo图片 -->
            <div class="flex items-center gap-3">
                <img th:src="${site.logoImageUrl}" 
                     th:alt="${site.logoAltText}"
                     class="w-8 h-8 object-contain rounded-lg">
                <span class="text-xl font-semibold text-gray-800" th:text="${site.siteName}">ChatFlow</span>
            </div>
            <!-- 右侧CTA按钮 -->
            <div>
                <a th:href="${site.navCtaLink}" 
                   class="inline-flex items-center justify-center bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium px-5 py-2 rounded-full transition-colors shadow-sm"
                   th:text="${site.navCtaText}">开始免费试用</a>
            </div>
        </div>
    </header>
</body>
</html>
```

## 9. 依赖配置 (pom.xml添加)

```xml
<!-- MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>

<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- JSON Schema验证（可选） -->
<dependency>
    <groupId>com.networknt</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>1.0.76</version>
</dependency>
```

## 10. 应用配置 (application.properties)

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/chatflow_cms?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MyBatis Plus配置
mybatis-plus.mapper-locations=classpath:/mapper/*.xml
mybatis-plus.type-aliases-package=com.example.demo.entity
mybatis-plus.global-config.db-config.id-type=ASSIGN_UUID
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```


## 11.改后的Thymeleaf模板

### 1. hero.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Hero Section</title>
</head>
<body>
    <section th:fragment="hero" class="relative bg-gradient-to-b from-white to-gray-50 pt-16 pb-20 overflow-hidden">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center max-w-3xl mx-auto">
                <h1 class="text-4xl md:text-5xl font-extrabold tracking-tight text-gray-900 mb-6">
                    <span th:text="${blockContent.title}">企业级即时通讯</span><br>
                    <span th:text="${blockContent.titleHighlight}">让协作更快一步</span>
                </h1>
                <p class="text-lg text-gray-600 mb-10" th:text="${blockContent.subtitle}">安全、高效、可定制——专为现代企业打造的智能聊天平台，集成工作流与数据洞察。</p>
                <div class="flex flex-wrap gap-4 justify-center">
                    <a th:href="${blockContent.buttons[0].link}" 
                       class="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-full font-medium shadow-md transition"
                       th:text="${blockContent.buttons[0].text}">开始免费使用</a>
                    <a th:href="${blockContent.buttons[1].link}" 
                       class="bg-white border border-gray-300 hover:border-gray-400 text-gray-700 px-6 py-3 rounded-full font-medium shadow-sm transition"
                       th:text="${blockContent.buttons[1].text}">联系销售</a>
                </div>
            </div>
            <div class="mt-16 max-w-5xl mx-auto">
                <img th:src="${blockContent.image.url}" 
                     th:alt="${blockContent.image.alt}"
                     alt="团队协作界面" 
                     class="rounded-xl shadow-2xl border border-gray-200 w-full h-auto object-cover">
            </div>
        </div>
    </section>
</body>
</html>
```

### 2. features.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Features Section</title>
</head>
<body>
    <section th:fragment="features" class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900" th:text="${blockContent.title}">专为商务打造的特性</h2>
                <p class="text-gray-600 mt-2" th:text="${blockContent.subtitle}">从安全到效率，面面俱到</p>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100" th:each="feature : ${blockContent.features}">
                    <div class="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-700 mb-4 text-xl" th:text="${feature.icon}">🔒</div>
                    <h3 class="font-semibold text-gray-900 mb-2" th:text="${feature.title}">企业级安全</h3>
                    <p class="text-gray-500 text-sm" th:text="${feature.description}">端到端加密、SSO、DLP策略，满足合规需求。</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 3. team.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Team Section</title>
</head>
<body>
    <section th:fragment="team" class="py-20 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900" th:text="${blockContent.title}">核心团队</h2>
                <p class="text-gray-600 mt-2" th:text="${blockContent.subtitle}">来自全球顶尖企业的协作专家</p>
            </div>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <div class="text-center" th:each="member : ${blockContent.members}">
                    <img th:src="${member.avatar}" 
                         th:alt="${member.name}"
                         alt="团队成员" 
                         class="w-32 h-32 rounded-full mx-auto object-cover shadow-md border-2 border-white">
                    <h3 class="font-semibold mt-4" th:text="${member.name}">张伟</h3>
                    <p class="text-gray-500 text-sm" th:text="${member.position}">CEO & 创始人</p>
                    <p class="text-gray-400 text-xs mt-1" th:if="${member.bio}" th:text="${member.bio}">简介</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 4. stats.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Stats Section</title>
</head>
<body>
    <section th:fragment="stats" class="py-16 bg-indigo-600 text-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
                <div th:each="stat : ${blockContent.stats}">
                    <div class="text-4xl font-bold" th:text="${stat.value}">500+</div>
                    <div class="text-indigo-100 mt-2" th:text="${stat.label}">企业客户</div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 5. pricing.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Pricing Section</title>
</head>
<body>
    <section th:fragment="pricing" class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900" th:text="${blockContent.title}">灵活定价</h2>
                <p class="text-gray-600 mt-2" th:text="${blockContent.subtitle}">按需选择，无隐藏费用</p>
            </div>
            <div class="grid md:grid-cols-3 gap-8">
                <div th:each="plan : ${blockContent.plans}" 
                     class="bg-white p-8 rounded-2xl shadow-sm border border-gray-200"
                     th:classappend="${plan.isPopular} ? 'shadow-md border-2 border-indigo-200 relative' : ''">
                    
                    <span th:if="${plan.isPopular}" 
                          class="absolute top-0 right-8 bg-indigo-600 text-white px-3 py-1 text-sm rounded-b-lg">最受欢迎</span>
                    
                    <h3 class="text-xl font-semibold" th:text="${plan.name}">基础版</h3>
                    
                    <div class="mt-4">
                        <span th:if="${plan.price}" class="text-4xl font-bold">
                            ¥<span th:text="${plan.price}">49</span>
                            <span class="text-base font-normal text-gray-500" th:text="'/' + ${plan.priceUnit}">/月/人</span>
                        </span>
                        <span th:if="${!plan.price}" class="text-4xl font-bold" th:text="${plan.priceText}">定制</span>
                    </div>
                    
                    <ul class="mt-6 space-y-3 text-gray-600">
                        <li th:each="feature : ${plan.features}" class="flex items-center gap-2">
                            <span>✓</span>
                            <span th:text="${feature}">消息历史1年</span>
                        </li>
                    </ul>
                    
                    <a th:href="${plan.buttonLink}" 
                       class="mt-8 block w-full text-center py-2 rounded-full font-medium"
                       th:classappend="${plan.isPopular} ? 'bg-indigo-600 text-white hover:bg-indigo-700 shadow' : 'border border-indigo-600 text-indigo-600 hover:bg-indigo-50'"
                       th:text="${plan.buttonText}">选择基础版</a>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6. faq.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">FAQ Section</title>
</head>
<body>
    <section th:fragment="faq" class="py-20 bg-white">
        <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 class="text-3xl font-bold text-center text-gray-900 mb-12" th:text="${blockContent.title}">常见问题</h2>
            <div class="space-y-6">
                <div th:each="faq : ${blockContent.faqs}" class="border-b border-gray-200 pb-6" th:classappend="${!faqStat.last} ? 'border-b border-gray-200 pb-6' : ''">
                    <h3 class="text-lg font-semibold text-gray-800 mb-2" th:text="${faq.question}">支持本地部署吗？</h3>
                    <p class="text-gray-600" th:text="${faq.answer}">是的，企业版支持私有云或本地服务器部署，满足最高安全合规要求。</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 7. left-image-right-text.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Left Image Right Text Section</title>
</head>
<body>
    <section th:fragment="leftImageRightText" class="py-20 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="grid md:grid-cols-2 gap-12 items-center">
                <div class="order-2 md:order-1">
                    <img th:src="${blockContent.image.url}" 
                         th:alt="${blockContent.image.alt}"
                         alt="团队沟通" 
                         class="rounded-2xl shadow-lg border border-gray-200 w-full h-auto object-cover">
                </div>
                <div class="order-1 md:order-2">
                    <h2 class="text-3xl font-bold text-gray-900 mb-4" th:text="${blockContent.title}">无缝沟通，跨越部门</h2>
                    <p class="text-gray-600 text-lg leading-relaxed" th:text="${blockContent.content}">打破信息孤岛，通过话题群组、私聊和富媒体分享，让每个人都能快速找到所需信息。集成企业目录，一键联系同事。</p>
                    <div class="mt-6 flex gap-4 text-sm text-indigo-600 font-medium" th:if="${blockContent.highlights}">
                        <span th:each="highlight : ${blockContent.highlights}" class="flex items-center gap-1">
                            <span>✓</span>
                            <span th:text="${highlight}">端到端加密</span>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 8. left-text-right-image.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Left Text Right Image Section</title>
</head>
<body>
    <section th:fragment="leftTextRightImage" class="py-20 bg-gray-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="grid md:grid-cols-2 gap-12 items-center">
                <div>
                    <h2 class="text-3xl font-bold text-gray-900 mb-4" th:text="${blockContent.title}">深度集成工作流</h2>
                    <p class="text-gray-600 text-lg leading-relaxed" th:text="${blockContent.content}">与您使用的工具无缝连接：Jira、GitLab、Google Drive、Salesforce。在聊天中创建任务、分享文件、触发自动化。</p>
                    <div class="mt-6 flex flex-wrap gap-3" th:if="${blockContent.tags}">
                        <span th:each="tag : ${blockContent.tags}" 
                              class="bg-indigo-50 text-indigo-700 px-4 py-2 rounded-full text-sm font-medium"
                              th:text="${tag}">Slack 导入</span>
                    </div>
                </div>
                <div>
                    <img th:src="${blockContent.image.url}" 
                         th:alt="${blockContent.image.alt}"
                         alt="工作流集成" 
                         class="rounded-2xl shadow-lg border border-gray-200 w-full h-auto object-cover">
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 9. top-text-bottom-image.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Top Text Bottom Image Section</title>
</head>
<body>
    <section th:fragment="topTextBottomImage" class="py-20 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h2 class="text-3xl font-bold text-gray-900 mb-3" th:text="${blockContent.title}">全平台一致体验</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto" th:text="${blockContent.description}">无论是在桌面、网页还是移动端，消息实时同步，操作流畅如一。</p>
            <div class="mt-12">
                <img th:src="${blockContent.image.url}" 
                     th:alt="${blockContent.image.alt}"
                     alt="多设备" 
                     class="rounded-2xl shadow-xl border border-gray-200 w-full h-auto object-cover max-w-5xl mx-auto">
            </div>
        </div>
    </section>
</body>
</html>
```

### 10. cta.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">CTA Section</title>
</head>
<body>
    <section th:fragment="cta" class="bg-indigo-600 py-16">
        <div class="max-w-3xl mx-auto text-center px-4 sm:px-6 lg:px-8">
            <h2 class="text-3xl font-bold text-white mb-4" th:text="${blockContent.title}">立即提升团队协作效率</h2>
            <p class="text-indigo-100 text-lg mb-8" th:text="${blockContent.subtitle}">加入数百家信任我们的企业，开启高效沟通之旅。</p>
            <div class="flex flex-wrap gap-4 justify-center">
                <a th:href="${blockContent.buttons[0].link}" 
                   class="bg-white text-indigo-600 hover:bg-gray-100 px-6 py-3 rounded-full font-medium shadow-lg transition"
                   th:text="${blockContent.buttons[0].text}">免费试用30天</a>
                <a th:href="${blockContent.buttons[1].link}" 
                   class="border border-white text-white hover:bg-indigo-500 px-6 py-3 rounded-full font-medium transition"
                   th:text="${blockContent.buttons[1].text}">预约演示</a>
            </div>
        </div>
    </section>
</body>
</html>
```

这样就完成了使用 MybatisPlus 的完整 Service 层和 Mapper 层实现。所有模板都通过 `blockContent` 变量接收从数据库获取的内容数据。
