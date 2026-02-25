-- 创建数据库
CREATE DATABASE IF NOT EXISTS chatflow_cms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE chatflow_cms;

-- =====================================================
-- 1. 站点表 (sites) - 主键UUID，Logo为图片类型
-- =====================================================
CREATE TABLE sites (
    id VARCHAR(32) PRIMARY KEY COMMENT '站点ID（32位UUID，不含横线）',
    site_name VARCHAR(100) NOT NULL COMMENT '站点名称',
    site_domain VARCHAR(255) COMMENT '站点域名（可选，用于多站点识别）',
    site_description TEXT COMMENT '站点描述',
    
    -- Logo配置（图片类型）
    logo_image_url VARCHAR(500) NOT NULL COMMENT '图片LogoURL',
    logo_alt_text VARCHAR(200) COMMENT 'Logo替代文本',
    
    -- 导航栏CTA按钮配置
    nav_cta_text VARCHAR(50) NOT NULL DEFAULT '开始免费试用' COMMENT '导航栏CTA按钮文字',
    nav_cta_link VARCHAR(500) NOT NULL DEFAULT '#' COMMENT '导航栏CTA按钮链接',
    
    -- SEO配置
    meta_title VARCHAR(200) COMMENT 'SEO标题',
    meta_description TEXT COMMENT 'SEO描述',
    meta_keywords VARCHAR(500) COMMENT 'SEO关键词',
    
    -- 状态和时间
    site_status TINYINT DEFAULT 1 COMMENT '站点状态：0禁用，1启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(50) COMMENT '创建人',
    
    INDEX idx_site_status (site_status),
    INDEX idx_site_domain (site_domain)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站点表';

-- =====================================================
-- 2. 页面表 (pages) - 主键UUID，去掉page_title字段
-- =====================================================
CREATE TABLE pages (
    id VARCHAR(32) PRIMARY KEY COMMENT '页面ID（32位UUID，不含横线）',
    site_id VARCHAR(32) NOT NULL COMMENT '所属站点ID',
    
    -- 页面基本信息
    page_name VARCHAR(100) NOT NULL COMMENT '页面名称',
    page_slug VARCHAR(100) NOT NULL COMMENT '页面URL路径（如：/about, /products）',
    page_description TEXT COMMENT '页面描述',
    
    -- 页面配置
    page_status TINYINT DEFAULT 1 COMMENT '页面状态：0草稿，1已发布',
    page_sort INT DEFAULT 0 COMMENT '页面排序（升序）',
    
    -- SEO配置（可覆盖站点配置）
    page_meta_title VARCHAR(200) COMMENT '页面SEO标题',
    page_meta_description TEXT COMMENT '页面SEO描述',
    page_meta_keywords VARCHAR(500) COMMENT '页面SEO关键词',
    
    -- 时间记录
    published_at DATETIME COMMENT '发布时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE,
    INDEX idx_site_id (site_id),
    INDEX idx_page_slug (page_slug),
    INDEX idx_page_status (page_status),
    UNIQUE KEY uk_site_page_slug (site_id, page_slug)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面表';

-- =====================================================
-- 3. 区块表 (blocks) - 主键UUID
-- =====================================================
CREATE TABLE blocks (
    id VARCHAR(32) PRIMARY KEY COMMENT '区块ID（32位UUID，不含横线）',
    site_id VARCHAR(32) NOT NULL COMMENT '所属站点ID',
    page_id VARCHAR(32) NOT NULL COMMENT '所属页面ID',
    
    -- 区块基本信息
    block_type VARCHAR(50) NOT NULL COMMENT '区块类型（hero, features, team, pricing, cta等）',
    block_name VARCHAR(100) NOT NULL COMMENT '区块名称',
    block_description TEXT COMMENT '区块描述',
    
    -- 区块内容配置（JSON格式，只存储内容数据，不包含任何样式）
    block_content JSON NOT NULL COMMENT '区块内容数据（纯内容，无样式）',
    
    -- 区块状态和排序
    block_status TINYINT DEFAULT 1 COMMENT '区块状态：0隐藏，1显示',
    block_sort INT DEFAULT 0 COMMENT '区块排序（升序）',
    
    -- 时间记录
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(50) COMMENT '创建人',
    
    FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE,
    FOREIGN KEY (page_id) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_site_page (site_id, page_id),
    INDEX idx_block_type (block_type),
    INDEX idx_block_status (block_status),
    INDEX idx_block_sort (block_sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区块表';

-- =====================================================
-- 4. 区块类型字典表 (block_types) - 主键自增整数（字典表保持整数主键）
-- =====================================================
CREATE TABLE block_types (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '类型ID',
    type_code VARCHAR(50) NOT NULL UNIQUE COMMENT '类型代码（如：hero, features）',
    type_name VARCHAR(100) NOT NULL COMMENT '类型名称',
    type_description TEXT COMMENT '类型描述',
    template_file VARCHAR(200) NOT NULL COMMENT '对应的Thymeleaf模板文件路径',
    
    -- JSON Schema定义，用于验证block_content的数据结构
    content_schema JSON COMMENT '内容JSON Schema（定义需要哪些字段）',
    
    -- 区块分类
    category VARCHAR(50) DEFAULT 'common' COMMENT '区块分类',
    sort INT DEFAULT 0 COMMENT '排序',
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_category (category),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区块类型字典表';

-- =====================================================
-- 生成UUID的函数（用于插入数据时使用）
-- =====================================================
DELIMITER $$
CREATE FUNCTION UUID_SHORT_NO_DASH() RETURNS VARCHAR(32)
DETERMINISTIC
BEGIN
    RETURN REPLACE(UUID(), '-', '');
END$$
DELIMITER ;

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入区块类型字典数据
INSERT INTO block_types (type_code, type_name, template_file, category, sort, content_schema) VALUES
('hero', 'Hero区块', 'fragments/sections/hero', 'header', 10, 
 '{"type":"object","required":["title"],"properties":{"title":{"type":"string"},"subtitle":{"type":"string"},"buttons":{"type":"array"}}}'),

('features', '特性区块', 'fragments/sections/features', 'content', 20,
 '{"type":"object","required":["title","features"],"properties":{"title":{"type":"string"},"features":{"type":"array"}}}'),

('team', '团队区块', 'fragments/sections/team', 'about', 30,
 '{"type":"object","properties":{"title":{"type":"string"},"members":{"type":"array"}}}'),

('pricing', '价格区块', 'fragments/sections/pricing', 'conversion', 40,
 '{"type":"object","required":["title","plans"],"properties":{"title":{"type":"string"},"plans":{"type":"array"}}}'),

('faq', '问题区块', 'fragments/sections/faq', 'support', 50,
 '{"type":"object","properties":{"title":{"type":"string"},"faqs":{"type":"array"}}}'),

('cta', '号召区块', 'fragments/sections/cta', 'conversion', 60,
 '{"type":"object","required":["title"],"properties":{"title":{"type":"string"},"subtitle":{"type":"string"},"buttons":{"type":"array"}}}'),

('left-image-right-text', '左图右文', 'fragments/sections/left-image-right-text', 'content', 15,
 '{"type":"object","required":["title","image"],"properties":{"title":{"type":"string"},"content":{"type":"string"},"image":{"type":"object"}}}'),

('left-text-right-image', '左文右图', 'fragments/sections/left-text-right-image', 'content', 16,
 '{"type":"object","required":["title","image"],"properties":{"title":{"type":"string"},"content":{"type":"string"},"image":{"type":"object"}}}'),

('top-text-bottom-image', '上文下图', 'fragments/sections/top-text-bottom-image', 'content', 17,
 '{"type":"object","required":["title","image"],"properties":{"title":{"type":"string"},"description":{"type":"string"},"image":{"type":"object"}}}'),

('stats', '统计区块', 'fragments/sections/stats', 'content', 25,
 '{"type":"object","required":["stats"],"properties":{"stats":{"type":"array"}}}');

-- 插入示例站点（使用UUID）
INSERT INTO sites (id, site_name, logo_image_url, logo_alt_text, nav_cta_text, nav_cta_link, meta_title, meta_description) VALUES
(UUID_SHORT_NO_DASH(), 'ChatFlow 官网', 
 'https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=64&h=64&fit=crop', 
 'ChatFlow Logo',
 '开始免费试用', 
 '/signup',
 'ChatFlow - 企业级即时通讯平台',
 '为企业打造的安全、高效、可定制的智能聊天平台');

-- 插入示例页面
INSERT INTO pages (id, site_id, page_name, page_slug, page_description, page_status, page_sort, page_meta_title) VALUES
(UUID_SHORT_NO_DASH(), (SELECT id FROM sites LIMIT 1), '首页', '/', 'ChatFlow官方网站首页，提供企业级即时通讯解决方案', 1, 0, 'ChatFlow - 企业级即时通讯平台');

-- 为首页插入各种区块示例
SET @site_id = (SELECT id FROM sites LIMIT 1);
SET @home_page_id = (SELECT id FROM pages WHERE page_slug = '/' LIMIT 1);

-- Hero区块
INSERT INTO blocks (id, site_id, page_id, block_type, block_name, block_content, block_sort) VALUES
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'hero', '首页Hero区块', 
 '{"title": "企业级即时通讯", "titleHighlight": "让协作更快一步", "subtitle": "安全、高效、可定制", "description": "专为现代企业打造的智能聊天平台，集成工作流与数据洞察。", "buttons": [{"text": "开始免费使用", "link": "/signup"}, {"text": "联系销售", "link": "/contact"}], "image": {"url": "https://images.unsplash.com/photo-1557804506-669a67965ba0", "alt": "团队协作界面"}}', 
 0),

-- 左图右文区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'left-image-right-text', '无缝沟通', 
 '{"title": "无缝沟通，跨越部门", "content": "打破信息孤岛，通过话题群组、私聊和富媒体分享，让每个人都能快速找到所需信息。集成企业目录，一键联系同事。", "highlights": ["端到端加密", "无限历史记录"], "image": {"url": "https://images.unsplash.com/photo-1522071820081-009f0129c71c", "alt": "团队沟通"}}',
 10),

-- 左文右图区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'left-text-right-image', '深度集成', 
 '{"title": "深度集成工作流", "content": "与您使用的工具无缝连接：Jira、GitLab、Google Drive、Salesforce。在聊天中创建任务、分享文件、触发自动化。", "tags": ["Slack 导入", "API 开放"], "image": {"url": "https://images.unsplash.com/photo-1551434678-e076c223a692", "alt": "工作流集成"}}',
 15),

-- 上文下图区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'top-text-bottom-image', '全平台体验', 
 '{"title": "全平台一致体验", "description": "无论是在桌面、网页还是移动端，消息实时同步，操作流畅如一。", "image": {"url": "https://images.unsplash.com/photo-1599305445671-ac291c95aaa9", "alt": "多设备支持"}}',
 20),

-- 特性区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'features', '核心特性', 
 '{"title": "专为商务打造的特性", "subtitle": "从安全到效率，面面俱到", "features": [{"icon": "🔒", "title": "企业级安全", "description": "端到端加密、SSO、DLP策略，满足合规需求。"}, {"icon": "⚡", "title": "实时同步", "description": "毫秒级延迟，跨设备已读回执与状态。"}, {"icon": "🧩", "title": "无限集成", "description": "连接200+企业应用，自定义机器人。"}, {"icon": "📊", "title": "分析洞察", "description": "团队活跃度、响应时间数据可视化。"}]}',
 25),

-- 团队区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'team', '核心团队', 
 '{"title": "核心团队", "subtitle": "来自全球顶尖企业的协作专家", "members": [{"name": "张伟", "position": "CEO & 创始人", "avatar": "https://images.unsplash.com/photo-1560250097-0b93528c311a", "bio": "前微软高级架构师"}, {"name": "陈敏", "position": "CTO", "avatar": "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2", "bio": "分布式系统专家"}, {"name": "王磊", "position": "产品总监", "avatar": "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7"}, {"name": "李莉", "position": "设计负责人", "avatar": "https://images.unsplash.com/photo-1580489944761-15a19d654956"}]}',
 30),

-- 统计区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'stats', '公司数据', 
 '{"stats": [{"value": "500+", "label": "企业客户"}, {"value": "98%", "label": "客户留存率"}, {"value": "20M+", "label": "日消息量"}, {"value": "24/7", "label": "技术支持"}]}',
 35),

-- 价格区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'pricing', '价格方案', 
 '{"title": "灵活定价", "subtitle": "按需选择，无隐藏费用", "plans": [{"name": "基础版", "price": 49, "priceUnit": "月/人", "features": ["消息历史1年", "10GB 文件存储", "基础集成"], "buttonText": "选择基础版", "buttonLink": "/signup/basic"}, {"name": "商业版", "price": 99, "priceUnit": "月/人", "features": ["无限历史", "100GB 存储", "所有集成 + API", "高级支持"], "buttonText": "选择商业版", "buttonLink": "/signup/business", "isPopular": true}, {"name": "企业版", "price": null, "priceText": "定制", "features": ["本地部署选项", "无限存储", "专属客户成功", "SSO/合规"], "buttonText": "联系销售", "buttonLink": "/contact"}]}',
 40),

-- 问题区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'faq', '常见问题', 
 '{"title": "常见问题", "faqs": [{"question": "支持本地部署吗？", "answer": "是的，企业版支持私有云或本地服务器部署，满足最高安全合规要求。"}, {"question": "可以试用多久？", "answer": "所有新用户均可享受30天全功能免费试用，无需信用卡。"}, {"question": "数据存储在哪里？", "answer": "数据存储在云端的独立数据库，可选中国大陆或海外区域，符合当地法规。"}, {"question": "如何迁移现有聊天记录？", "answer": "我们提供专业迁移工具，支持从Slack、Teams等平台导入历史数据。"}]}',
 45),

-- 号召区块
(UUID_SHORT_NO_DASH(), @site_id, @home_page_id, 'cta', '立即行动', 
 '{"title": "立即提升团队协作效率", "subtitle": "加入数百家信任我们的企业，开启高效沟通之旅。", "buttons": [{"text": "免费试用30天", "link": "/signup"}, {"text": "预约演示", "link": "/demo"}]}',
 50);