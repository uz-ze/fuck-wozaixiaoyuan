package uz.fuckwzxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "uz.fuckwzxy.mapper")
@EnableAsync
@EnableScheduling
public class FuckWoZaiXiaoYuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuckWoZaiXiaoYuanApplication.class, args);
    }

}
