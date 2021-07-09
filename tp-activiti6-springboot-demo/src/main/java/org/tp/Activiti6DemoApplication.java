package org.tp;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Activiti6DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Activiti6DemoApplication.class, args);
	}
}
