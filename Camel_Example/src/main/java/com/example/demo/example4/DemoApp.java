package com.example.demo.example4;

import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class DemoApp {

	private static final String DESTINATION1 = "file:input";
	private static final String DESTINATION2 = "activemq:queue:demo-queue";
	
	

	/**
	 * 
	 * This will transfer file from file system to activeMQ
	 * To run activeMQ in docker 
	 * run "docker run --name='activemq' -d -p 8161:8161 -p 61616:61616 -p 61613:61613 webcenter/activemq:5.14.3"
	 * ActiveMQ will start in 8161 port with credential admin:admin
	 * 
	 */
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION1).to(DESTINATION2);
			}
		});
		while (true)
			context.start();

	}

}
