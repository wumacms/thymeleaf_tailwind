# ChatFlow 落地页 — 技术实现指南

## 1. 项目结构

```
src/main/
├── java/com/example/demo/
│   ├── DemoApplication.java
│   └── controller/
│       └── HomeController.java
├── resources/
│   ├── templates/
│   │   ├── fragments/
│   │   │   ├── header.html
│   │   │   ├── footer.html
│   │   │   └── sections/
│   │   │       ├── hero.html
│   │   │       ├── left-image-right-text.html
│   │   │       ├── left-text-right-image.html
│   │   │       ├── top-text-bottom-image.html
│   │   │       ├── features.html
│   │   │       ├── team.html
│   │   │       ├── stats.html
│   │   │       ├── pricing.html
│   │   │       ├── faq.html
│   │   │       └── cta.html
│   │   └── index.html
│   └── application.properties
└── pom.xml
```

## 2. Maven依赖 (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>chat-app-landing</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>chat-app-landing</name>
    <description>企业聊天APP宣传落地页</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 3. 配置文件 (application.properties)

```properties
# 应用配置
spring.application.name=chat-app-landing
server.port=8080

# Thymeleaf配置
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML

# 开发工具
spring.devtools.restart.enabled=true
```

## 4. 主应用类 (DemoApplication.java)

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## 5. 控制器 (HomeController.java)

```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
```

## 6. Thymeleaf模板文件

### 6.1 头部片段 (fragments/header.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:remove="all">Header Fragment</title>
</head>
<body>
    <header th:fragment="header" class="border-b border-gray-100 bg-white/80 backdrop-blur-sm sticky top-0 z-10">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
            <!-- 左侧 Logo占位 + App名称 -->
            <div class="flex items-center gap-3">
                <div class="w-8 h-8 bg-gradient-to-br from-indigo-500 to-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-sm shadow-sm">C</div>
                <span class="text-xl font-semibold text-gray-800">ChatFlow</span>
            </div>
            <!-- 右侧 CTA按钮 -->
            <div>
                <a href="#" class="inline-flex items-center justify-center bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium px-5 py-2 rounded-full transition-colors shadow-sm">开始免费试用</a>
            </div>
        </div>
    </header>
</body>
</html>
```

### 6.2 底部片段 (fragments/footer.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:remove="all">Footer Fragment</title>
</head>
<body>
    <footer th:fragment="footer" class="bg-gray-900 text-gray-300 py-8">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row items-center justify-between">
            <div class="flex items-center gap-2">
                <div class="w-6 h-6 bg-indigo-500 rounded-md flex items-center justify-center text-white text-xs">C</div>
                <span class="font-semibold text-white">ChatFlow</span>
            </div>
            <div class="text-sm mt-4 md:mt-0">
                © 2025 ChatFlow Technologies · 企业聊天解决方案。 保留所有权利。
            </div>
        </div>
    </footer>
</body>
</html>
```

### 6.3 Hero区块 (fragments/sections/hero.html)

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
                <h1 class="text-4xl md:text-5xl font-extrabold tracking-tight text-gray-900 mb-6">企业级即时通讯<br>让协作更快一步</h1>
                <p class="text-lg text-gray-600 mb-10">安全、高效、可定制——专为现代企业打造的智能聊天平台，集成工作流与数据洞察。</p>
                <div class="flex flex-wrap gap-4 justify-center">
                    <a href="#" class="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-full font-medium shadow-md transition">开始免费使用</a>
                    <a href="#" class="bg-white border border-gray-300 hover:border-gray-400 text-gray-700 px-6 py-3 rounded-full font-medium shadow-sm transition">联系销售</a>
                </div>
            </div>
            <div class="mt-16 max-w-5xl mx-auto">
                <img src="https://images.unsplash.com/photo-1557804506-669a67965ba0?ixlib=rb-4.0.3&auto=format&fit=crop&w=1600&q=80" 
                     alt="团队协作界面" 
                     class="rounded-xl shadow-2xl border border-gray-200 w-full h-auto object-cover">
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.4 左图右文区块 (fragments/sections/left-image-right-text.html)

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
                    <img src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" 
                         alt="团队沟通" 
                         class="rounded-2xl shadow-lg border border-gray-200 w-full h-auto object-cover">
                </div>
                <div class="order-1 md:order-2">
                    <h2 class="text-3xl font-bold text-gray-900 mb-4">无缝沟通，跨越部门</h2>
                    <p class="text-gray-600 text-lg leading-relaxed">打破信息孤岛，通过话题群组、私聊和富媒体分享，让每个人都能快速找到所需信息。集成企业目录，一键联系同事。</p>
                    <div class="mt-6 flex gap-4 text-sm text-indigo-600 font-medium">
                        <span class="flex items-center gap-1">✓ 端到端加密</span>
                        <span class="flex items-center gap-1">✓ 无限历史记录</span>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.5 左文右图区块 (fragments/sections/left-text-right-image.html)

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
                    <h2 class="text-3xl font-bold text-gray-900 mb-4">深度集成工作流</h2>
                    <p class="text-gray-600 text-lg leading-relaxed">与您使用的工具无缝连接：Jira、GitLab、Google Drive、Salesforce。在聊天中创建任务、分享文件、触发自动化。</p>
                    <div class="mt-6 flex flex-wrap gap-3">
                        <span class="bg-indigo-50 text-indigo-700 px-4 py-2 rounded-full text-sm font-medium">Slack 导入</span>
                        <span class="bg-indigo-50 text-indigo-700 px-4 py-2 rounded-full text-sm font-medium">API 开放</span>
                    </div>
                </div>
                <div>
                    <img src="https://images.unsplash.com/photo-1551434678-e076c223a692?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" 
                         alt="工作流集成" 
                         class="rounded-2xl shadow-lg border border-gray-200 w-full h-auto object-cover">
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.6 上文下图区块 (fragments/sections/top-text-bottom-image.html)

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
            <h2 class="text-3xl font-bold text-gray-900 mb-3">全平台一致体验</h2>
            <p class="text-gray-600 text-lg max-w-2xl mx-auto">无论是在桌面、网页还是移动端，消息实时同步，操作流畅如一。</p>
            <div class="mt-12">
                <img src="https://images.unsplash.com/photo-1599305445671-ac291c95aaa9?ixlib=rb-4.0.3&auto=format&fit=crop&w=1600&q=80" 
                     alt="多设备" 
                     class="rounded-2xl shadow-xl border border-gray-200 w-full h-auto object-cover max-w-5xl mx-auto">
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.7 特性区块 (fragments/sections/features.html)

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
                <h2 class="text-3xl font-bold text-gray-900">专为商务打造的特性</h2>
                <p class="text-gray-600 mt-2">从安全到效率，面面俱到</p>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div class="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-700 mb-4 text-xl">🔒</div>
                    <h3 class="font-semibold text-gray-900 mb-2">企业级安全</h3>
                    <p class="text-gray-500 text-sm">端到端加密、SSO、DLP策略，满足合规需求。</p>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div class="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-700 mb-4 text-xl">⚡</div>
                    <h3 class="font-semibold text-gray-900 mb-2">实时同步</h3>
                    <p class="text-gray-500 text-sm">毫秒级延迟，跨设备已读回执与状态。</p>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div class="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-700 mb-4 text-xl">🧩</div>
                    <h3 class="font-semibold text-gray-900 mb-2">无限集成</h3>
                    <p class="text-gray-500 text-sm">连接200+企业应用，自定义机器人。</p>
                </div>
                <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div class="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-700 mb-4 text-xl">📊</div>
                    <h3 class="font-semibold text-gray-900 mb-2">分析洞察</h3>
                    <p class="text-gray-500 text-sm">团队活跃度、响应时间数据可视化。</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.8 团队区块 (fragments/sections/team.html)

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
                <h2 class="text-3xl font-bold text-gray-900">核心团队</h2>
                <p class="text-gray-600 mt-2">来自全球顶尖企业的协作专家</p>
            </div>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <div class="text-center">
                    <img src="https://images.unsplash.com/photo-1560250097-0b93528c311a?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" 
                         alt="团队成员" 
                         class="w-32 h-32 rounded-full mx-auto object-cover shadow-md border-2 border-white">
                    <h3 class="font-semibold mt-4">张伟</h3>
                    <p class="text-gray-500 text-sm">CEO & 创始人</p>
                </div>
                <div class="text-center">
                    <img src="https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" 
                         alt="团队成员" 
                         class="w-32 h-32 rounded-full mx-auto object-cover shadow-md border-2 border-white">
                    <h3 class="font-semibold mt-4">陈敏</h3>
                    <p class="text-gray-500 text-sm">CTO</p>
                </div>
                <div class="text-center">
                    <img src="https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" 
                         alt="团队成员" 
                         class="w-32 h-32 rounded-full mx-auto object-cover shadow-md border-2 border-white">
                    <h3 class="font-semibold mt-4">王磊</h3>
                    <p class="text-gray-500 text-sm">产品总监</p>
                </div>
                <div class="text-center">
                    <img src="https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" 
                         alt="团队成员" 
                         class="w-32 h-32 rounded-full mx-auto object-cover shadow-md border-2 border-white">
                    <h3 class="font-semibold mt-4">李莉</h3>
                    <p class="text-gray-500 text-sm">设计负责人</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.9 统计区块 (fragments/sections/stats.html)

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
                <div>
                    <div class="text-4xl font-bold">500+</div>
                    <div class="text-indigo-100 mt-2">企业客户</div>
                </div>
                <div>
                    <div class="text-4xl font-bold">98%</div>
                    <div class="text-indigo-100 mt-2">客户留存率</div>
                </div>
                <div>
                    <div class="text-4xl font-bold">20M+</div>
                    <div class="text-indigo-100 mt-2">日消息量</div>
                </div>
                <div>
                    <div class="text-4xl font-bold">24/7</div>
                    <div class="text-indigo-100 mt-2">技术支持</div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.10 价格区块 (fragments/sections/pricing.html)

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
                <h2 class="text-3xl font-bold text-gray-900">灵活定价</h2>
                <p class="text-gray-600 mt-2">按需选择，无隐藏费用</p>
            </div>
            <div class="grid md:grid-cols-3 gap-8">
                <div class="bg-white p-8 rounded-2xl shadow-sm border border-gray-200">
                    <h3 class="text-xl font-semibold">基础版</h3>
                    <p class="text-4xl font-bold mt-4">¥49<span class="text-base font-normal text-gray-500">/月/人</span></p>
                    <ul class="mt-6 space-y-3 text-gray-600">
                        <li class="flex items-center gap-2">✓ 消息历史1年</li>
                        <li class="flex items-center gap-2">✓ 10GB 文件存储</li>
                        <li class="flex items-center gap-2">✓ 基础集成</li>
                    </ul>
                    <a href="#" class="mt-8 block w-full text-center border border-indigo-600 text-indigo-600 hover:bg-indigo-50 py-2 rounded-full font-medium">选择基础版</a>
                </div>
                <div class="bg-white p-8 rounded-2xl shadow-md border-2 border-indigo-200 relative">
                    <span class="absolute top-0 right-8 bg-indigo-600 text-white px-3 py-1 text-sm rounded-b-lg">最受欢迎</span>
                    <h3 class="text-xl font-semibold">商业版</h3>
                    <p class="text-4xl font-bold mt-4">¥99<span class="text-base font-normal text-gray-500">/月/人</span></p>
                    <ul class="mt-6 space-y-3 text-gray-600">
                        <li class="flex items-center gap-2">✓ 无限历史</li>
                        <li class="flex items-center gap-2">✓ 100GB 存储</li>
                        <li class="flex items-center gap-2">✓ 所有集成 + API</li>
                        <li class="flex items-center gap-2">✓ 高级支持</li>
                    </ul>
                    <a href="#" class="mt-8 block w-full text-center bg-indigo-600 text-white hover:bg-indigo-700 py-2 rounded-full font-medium shadow">选择商业版</a>
                </div>
                <div class="bg-white p-8 rounded-2xl shadow-sm border border-gray-200">
                    <h3 class="text-xl font-semibold">企业版</h3>
                    <p class="text-4xl font-bold mt-4">定制</p>
                    <ul class="mt-6 space-y-3 text-gray-600">
                        <li class="flex items-center gap-2">✓ 本地部署选项</li>
                        <li class="flex items-center gap-2">✓ 无限存储</li>
                        <li class="flex items-center gap-2">✓ 专属客户成功</li>
                        <li class="flex items-center gap-2">✓ SSO/合规</li>
                    </ul>
                    <a href="#" class="mt-8 block w-full text-center border border-gray-300 text-gray-700 hover:bg-gray-50 py-2 rounded-full font-medium">联系销售</a>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.11 问题区块 (fragments/sections/faq.html)

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
            <h2 class="text-3xl font-bold text-center text-gray-900 mb-12">常见问题</h2>
            <div class="space-y-6">
                <div class="border-b border-gray-200 pb-6">
                    <h3 class="text-lg font-semibold text-gray-800 mb-2">支持本地部署吗？</h3>
                    <p class="text-gray-600">是的，企业版支持私有云或本地服务器部署，满足最高安全合规要求。</p>
                </div>
                <div class="border-b border-gray-200 pb-6">
                    <h3 class="text-lg font-semibold text-gray-800 mb-2">可以试用多久？</h3>
                    <p class="text-gray-600">所有新用户均可享受30天全功能免费试用，无需信用卡。</p>
                </div>
                <div class="border-b border-gray-200 pb-6">
                    <h3 class="text-lg font-semibold text-gray-800 mb-2">数据存储在哪里？</h3>
                    <p class="text-gray-600">数据存储在云端的独立数据库，可选中国大陆或海外区域，符合当地法规。</p>
                </div>
                <div>
                    <h3 class="text-lg font-semibold text-gray-800 mb-2">如何迁移现有聊天记录？</h3>
                    <p class="text-gray-600">我们提供专业迁移工具，支持从Slack、Teams等平台导入历史数据。</p>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.12 号召区块 (fragments/sections/cta.html)

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
            <h2 class="text-3xl font-bold text-white mb-4">立即提升团队协作效率</h2>
            <p class="text-indigo-100 text-lg mb-8">加入数百家信任我们的企业，开启高效沟通之旅。</p>
            <div class="flex flex-wrap gap-4 justify-center">
                <a href="#" class="bg-white text-indigo-600 hover:bg-gray-100 px-6 py-3 rounded-full font-medium shadow-lg transition">免费试用30天</a>
                <a href="#" class="border border-white text-white hover:bg-indigo-500 px-6 py-3 rounded-full font-medium transition">预约演示</a>
            </div>
        </div>
    </section>
</body>
</html>
```

### 6.13 主页面 (index.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ChatFlow - 企业级即时通讯</title>
    <!-- Tailwind via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-white text-gray-800 antialiased">

    <!-- 导航栏 -->
    <div th:replace="~{fragments/header :: header}"></div>

    <main>
        <!-- Hero区块 -->
        <div th:replace="~{fragments/sections/hero :: hero}"></div>
        
        <!-- 左图右文区块 -->
        <div th:replace="~{fragments/sections/left-image-right-text :: leftImageRightText}"></div>
        
        <!-- 左文右图区块 -->
        <div th:replace="~{fragments/sections/left-text-right-image :: leftTextRightImage}"></div>
        
        <!-- 上文下图区块 -->
        <div th:replace="~{fragments/sections/top-text-bottom-image :: topTextBottomImage}"></div>
        
        <!-- 特性区块 -->
        <div th:replace="~{fragments/sections/features :: features}"></div>
        
        <!-- 团队区块 -->
        <div th:replace="~{fragments/sections/team :: team}"></div>
        
        <!-- 统计区块 -->
        <div th:replace="~{fragments/sections/stats :: stats}"></div>
        
        <!-- 价格区块 -->
        <div th:replace="~{fragments/sections/pricing :: pricing}"></div>
        
        <!-- 问题区块 -->
        <div th:replace="~{fragments/sections/faq :: faq}"></div>
        
        <!-- 号召区块 -->
        <div th:replace="~{fragments/sections/cta :: cta}"></div>
    </main>

    <!-- 页脚 -->
    <div th:replace="~{fragments/footer :: footer}"></div>

</body>
</html>
```

## 7. 运行项目

1. 将以上所有文件按照项目结构创建
2. 在项目根目录执行：
  ```bash
   mvn spring-boot:run
  ```
3. 打开浏览器访问：[http://localhost:18080](http://localhost:18080)

## 8. 项目特点

- **模块化设计**：每个区块都是独立的Thymeleaf片段，便于维护和复用
- **无JavaScript**：完全符合要求，纯HTML+CSS
- **Tailwind CSS**：通过CDN引入，无需额外配置
- **响应式布局**：在移动端和桌面端都能良好显示
- **开发友好**：配置了devtools，修改模板后自动重启

现在您可以通过浏览器访问完整的落地页了！所有区块都已正确拆分并组合。