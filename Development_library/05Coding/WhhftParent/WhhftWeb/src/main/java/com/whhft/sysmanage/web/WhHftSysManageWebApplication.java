package com.whhft.sysmanage.web;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Configuration  
@ComponentScan
@ServletComponentScan
@ImportResource(value = {"classpath:/rmi_client.xml","classpath:/shiro-context.xml"})
public class WhHftSysManageWebApplication extends SpringBootServletInitializer{
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	       return builder.sources(WhHftSysManageWebApplication.class);
    }
	public static void main(String[] args) throws Exception {
		SpringApplication.run(WhHftSysManageWebApplication.class, args);
		CountDownLatch closeLatch = new CountDownLatch(1);
        closeLatch.await();
	}
}
