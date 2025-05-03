package com.skillswap.SkillSwap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages={"com.skillswap.contollers","com.skillswap.Dao","com.skillswap.entity","com.skillswap.jpa"})
@EnableJpaRepositories(basePackages = "com.skillswap.jpa")
@EntityScan(basePackages = "com.skillswap.entity")

public class SkillSwapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillSwapApplication.class, args);
	}

}
