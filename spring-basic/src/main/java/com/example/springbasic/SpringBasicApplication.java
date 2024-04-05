package com.example.springbasic;

import com.example.springbasic.ch01.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBasicApplication.class, args);

		/*
		// ApplicationContext: Spring Container에 저장된 bean 객체 확인 가능
		ApplicationContext context = SpringApplication.run(SpringBasicApplication.class, args);

		//ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		String[] beanDefinitionNames = context.getBeanDefinitionNames(); // bean 이름을 가져온다

		for (String beanDefinitionName : beanDefinitionNames) {
			Object bean = context.getBean(beanDefinitionName); // bean 객체를 가져온다
			System.out.println("name: " + beanDefinitionName + ", object: " + bean);
		}
		*/
	}
}
