package system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: wzw
 * @Description: 启动类
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"system.dao"})
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run( SystemApplication.class );
    }
}
