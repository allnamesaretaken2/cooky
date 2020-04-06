package de.cooky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages= {"de.cooky"})
@EnableAutoConfiguration
public class CookyApp {
	
	public static void main(String[] args) {
		SpringApplication.run(CookyApp.class, args);
	}

}