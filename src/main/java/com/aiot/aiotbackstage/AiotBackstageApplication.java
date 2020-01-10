package com.aiot.aiotbackstage;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication
@MapperScan(basePackages = {"com.aiot.aiotbackstage.mapper"}) //扫描mapper
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class AiotBackstageApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiotBackstageApplication.class, args);
	}

}
