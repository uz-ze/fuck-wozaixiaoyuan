package com.fuckwzxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author DK
 */
@SpringBootApplication
@MapperScan(basePackages = "com.fuckwzxy.account.mapper")
@EnableAsync
@EnableScheduling
public class FuckwzxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuckwzxyApplication.class, args);
    }

}
