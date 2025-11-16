package com.medical.standard;

import com.medical.standard.entity.User;
import com.medical.standard.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库连接测试
 * 用于验证数据库连接是否成功
 */
@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

    /**
     * 测试1：验证数据源是否配置成功
     */
    @Test
    public void testDataSource() {
        assertNotNull(dataSource, "数据源不能为空");
        System.out.println("✅ 数据源配置成功");
    }

    /**
     * 测试2：验证数据库连接是否成功
     */
    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "数据库连接不能为空");
            assertFalse(connection.isClosed(), "数据库连接不应该是关闭状态");
            
            System.out.println("✅ 数据库连接成功");
            System.out.println("数据库URL: " + connection.getMetaData().getURL());
            System.out.println("数据库用户: " + connection.getMetaData().getUserName());
            System.out.println("数据库产品: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("数据库版本: " + connection.getMetaData().getDatabaseProductVersion());
        }
    }

    /**
     * 测试3：验证MyBatis-Plus是否配置成功
     */
    @Test
    public void testMyBatisPlusMapper() {
        assertNotNull(userMapper, "UserMapper不能为空");
        System.out.println("✅ MyBatis-Plus Mapper配置成功");
    }

    /**
     * 测试4：验证数据库表是否存在并能查询数据
     */
    @Test
    public void testQueryUsers() {
        List<User> users = userMapper.selectList(null);
        assertNotNull(users, "用户列表不能为空");
        
        System.out.println("✅ 数据库表查询成功");
        System.out.println("用户总数: " + users.size());
        
        if (!users.isEmpty()) {
            System.out.println("\n用户列表：");
            users.forEach(user -> {
                System.out.println("- UserID: " + user.getUserID() + 
                                 ", Username: " + user.getUsername() + 
                                 ", RoleType: " + user.getRoleType());
            });
        }
    }

    /**
     * 测试5：验证PascalCase字段映射是否正确
     */
    @Test
    public void testPascalCaseMapping() {
        List<User> users = userMapper.selectList(null);
        
        if (!users.isEmpty()) {
            User user = users.get(0);
            
            // 验证PascalCase字段是否正确映射
            assertNotNull(user.getUserID(), "UserID字段映射失败");
            assertNotNull(user.getUsername(), "Username字段映射失败");
            assertNotNull(user.getCreatedTime(), "CreatedTime字段映射失败");
            
            System.out.println("✅ PascalCase字段映射成功");
            System.out.println("示例用户数据：");
            System.out.println("  UserID: " + user.getUserID());
            System.out.println("  Username: " + user.getUsername());
            System.out.println("  RealName: " + user.getRealName());
            System.out.println("  RoleType: " + user.getRoleType());
            System.out.println("  CreatedTime: " + user.getCreatedTime());
        }
    }
}

