package com.whhft.sysmanage.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;

@Component
public class RmiServiceEndPoint implements ApplicationContextAware {
	
	@Value("${rmi.port}")
	private int rmiPort;
	
	private List<String> rmiEndpointConfig;
	
	private ApplicationContext context;
	@PostConstruct
	public void init() throws ClassNotFoundException, RemoteException{
		rmiEndpointConfig = (List<String>)context.getBean("rmiEndpointConfig");
		for(String configStr: rmiEndpointConfig){
			String[] params = configStr.split(",");
			RmiServiceExporter rse = new RmiServiceExporter();
			rse.setServiceName(params[0].trim());
			rse.setServiceInterface(Class.forName(params[1].trim()));
			rse.setRegistryPort(rmiPort);
			rse.setService(context.getBean(params[2].trim()));
			rse.prepare();
		}
		
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
