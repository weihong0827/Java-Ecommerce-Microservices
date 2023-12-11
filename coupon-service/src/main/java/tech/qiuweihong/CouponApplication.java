package tech.qiuweihong;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("tech.qiuweihong.mapper")
@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
public class CouponApplication {
    public static void main(String[] args) {


        SpringApplication.run(CouponApplication.class,args);
    }

}
