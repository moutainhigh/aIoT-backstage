package com.aiot.aiotbackstage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.aiot.aiotbackstage.mapper"}) //扫描mapper
public class AiotBackstageApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiotBackstageApplication.class, args);
	}

}
