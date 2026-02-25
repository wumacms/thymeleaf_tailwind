package com.example.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

                String username = getCurrentUsername();
                this.strictInsertFill(metaObject, "createdBy", String.class, username);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }

            private String getCurrentUsername() {
                try {
                    Object ctx = Class.forName("org.springframework.security.core.context.SecurityContextHolder")
                            .getMethod("getContext")
                            .invoke(null);
                    Object auth = ctx.getClass().getMethod("getAuthentication").invoke(ctx);
                    if (auth != null) {
                        Object principal = auth.getClass().getMethod("getPrincipal").invoke(auth);
                        if (principal != null && Class.forName("org.springframework.security.core.userdetails.UserDetails").isInstance(principal)) {
                            return (String) principal.getClass().getMethod("getUsername").invoke(principal);
                        }
                        return principal != null ? principal.toString() : "system";
                    }
                } catch (Exception ignored) {
                    // 无 Spring Security 或未登录时使用 system
                }
                return "system";
            }
        };
    }
}
