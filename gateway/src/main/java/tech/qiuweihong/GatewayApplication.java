package tech.qiuweihong;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.load();
//        dotenv.entries().forEach(entry -> {
//            System.out.println(entry.getKey()+": "+entry.getValue());
//            System.setProperty(entry.getKey(), entry.getValue());
//        });
        SpringApplication.run(GatewayApplication.class,args);
    }

}
