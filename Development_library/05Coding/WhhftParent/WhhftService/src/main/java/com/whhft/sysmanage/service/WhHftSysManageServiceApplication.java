package com.whhft.sysmanage.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.whhft.sysmanage.service.listener.ShareniuSpringApplicationEventListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.whhft.sysmanage.service.mapper")
@ImportResource(value = { "classpath:/rmi_server.xml" })
public class WhHftSysManageServiceApplication {

	public static void main(String[] args) throws Exception {

		// ApplicationContext ctx =
		// SpringApplication.run(WhHftSysManageServiceApplication.class, args);
		SpringApplication springApplication = new SpringApplication(WhHftSysManageServiceApplication.class);
		springApplication.addListeners(new ShareniuSpringApplicationEventListener());
		springApplication.run(args);
		// CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
		CountDownLatch closeLatch = new CountDownLatch(1);
		closeLatch.await();

		// Scanner sc = new Scanner(System.in);
		// while (true){
		// if ("x".equals(sc.nextLine())){
		// break;
		// }
		// }
	}

	

}
