package pers.bertram.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("pers.bertram.controller")
public class App {
		
	public static void main(String[] args) {
        SpringApplication.run(App.class, args);
	}

}
