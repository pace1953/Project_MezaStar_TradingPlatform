package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring boot 啟動完成前會先執行此配置
public class ModelMapperConfig {
	
	// Spring boot 會自動建立此物件並管理
	// 其他程式可以透過 @Autowired 來取得該實體物件
	// 第三方類別需要透過 @Bean 方法明確註冊到 Spring 容器，透過@Bean 註解可以在其他地方使用 @Autowired 注入
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}