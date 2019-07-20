package com.fengbiaoedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.fengbiaoedu.mapper","com.fengbiaoedu.owmapper"})//扫描多个mapper包
@ServletComponentScan("com.fengbiaoedu")
public class WisdomlabApplication {

	public static void main(String[] args) {
		SpringApplication.run(WisdomlabApplication.class, args);
	}
}
