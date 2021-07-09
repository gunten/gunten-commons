package org.tp;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot2.0不能与activiti6.0.0直接集成使用，因为activiti6.0.0出来的时候springboot2.0还没有出来，
 * activiti6.0.0 支持springboot1.2.6以上，2.0.0以下的版本。
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Activiti6DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Activiti6DemoApplication.class, args);
	}
}
