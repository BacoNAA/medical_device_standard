package com.medical.standard.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 自动填充处理器
     * 注意：字段名使用PascalCase（CreatedTime、UpdatedTime等）
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            /**
             * 插入时自动填充
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                // 自动填充创建时间和更新时间（PascalCase字段名）
                this.strictInsertFill(metaObject, "CreatedTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "UpdatedTime", LocalDateTime.class, LocalDateTime.now());

                // 注意：CreatedBy和UpdatedBy需要在Service层手动设置（从当前登录用户获取）
                // 这里不自动填充，因为需要从SecurityContext或ThreadLocal获取当前用户ID
            }

            /**
             * 更新时自动填充
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                // 自动填充更新时间（PascalCase字段名）
                this.strictUpdateFill(metaObject, "UpdatedTime", LocalDateTime.class, LocalDateTime.now());

                // 注意：UpdatedBy需要在Service层手动设置（从当前登录用户获取）
            }
        };
    }
}


