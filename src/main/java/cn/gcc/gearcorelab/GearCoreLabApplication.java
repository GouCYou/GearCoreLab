package cn.gcc.gearcorelab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Spring Boot自动配置注解，启用自动配置、组件扫描和配置属性
@MapperScan("cn.gcc.gearcorelab.mapper")  // MyBatis Mapper接口扫描，自动注册指定包下的Mapper接口
public class GearCoreLabApplication {
    
    /**
     * 应用程序主入口方法
     * 启动Spring Boot应用程序容器
     * 
     * @param args 命令行参数，可用于传递配置参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用程序，初始化Spring容器并启动内嵌的Web服务器
        SpringApplication.run(GearCoreLabApplication.class, args);
    }
}
