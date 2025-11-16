package com.medical.standard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.medical.standard.mapper")
public class MedicalStandardDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalStandardDatabaseApplication.class, args);
	}

}
