package com.nit.book.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.nit.book.shop.mapper", "com.nit.book.shop.dao"})
@EnableTransactionManagement
@SpringBootApplication
public class BookShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}
}
