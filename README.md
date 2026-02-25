# ChatFlow 落地页

企业聊天 APP 宣传落地页，基于 Spring Boot + Thymeleaf + Tailwind CSS 构建的单页展示站点。

## 技术栈

- **Java 17**
- **Spring Boot 3.1.5**
- **Thymeleaf** — 服务端模板与片段复用
- **Tailwind CSS** — 通过 CDN 引入，无需构建

## 环境要求

- JDK 17+
- Maven 3.6+

## 快速开始

```bash
# 克隆后进入项目目录
cd thymeleaf_tailwind

# 启动应用
mvn spring-boot:run
```

浏览器访问：**http://localhost:8080**

修改端口可在 `src/main/resources/application.properties` 中设置 `server.port`。

## 项目结构

```
src/main/
├── java/com/example/demo/
│   ├── DemoApplication.java          # 启动类
│   └── controller/
│       └── HomeController.java       # 首页控制器
└── resources/
    ├── application.properties       # 应用配置
    └── templates/
        ├── index.html                # 主页面
        └── fragments/                # 可复用片段
            ├── header.html
            ├── footer.html
            └── sections/             # 各内容区块
                ├── hero.html
                ├── left-image-right-text.html
                ├── left-text-right-image.html
                ├── top-text-bottom-image.html
                ├── features.html
                ├── team.html
                ├── stats.html
                ├── pricing.html
                ├── faq.html
                └── cta.html
```

## 构建与打包

```bash
# 打包
mvn clean package

# 运行 jar（跳过测试）
java -jar target/chat-app-landing-0.0.1-SNAPSHOT.jar
```

## 开发说明

- 已启用 **Spring DevTools**，修改模板或配置后会自动重启。
- 页面为模块化 Thymeleaf 片段组合，无前端构建步骤，样式由 Tailwind CDN 提供。
- 详细实现步骤见：[ChatFlow 落地页 — 技术实现指南](./ChatFlow%20落地页%20—%20技术实现指南.md)

## 许可

仅供学习与参考使用。
