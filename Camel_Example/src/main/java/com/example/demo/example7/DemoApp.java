package com.example.demo.example7;

import java.util.Date;

import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

public class DemoApp {

	private static final String DESTINATION1 = "direct:destination1";
	private static final String DESTINATION2 = "activemq:queue:object-queue";
	private static final String DESTINATION3 = "bean:myService?method=printString";

	/**
	 * @param args
	 * @throws Exception Example of calling a method of class when getting message
	 *                   in MQ using bean component
	 */
	public static void main(String[] args) throws Exception {
		// First we need to create registry and register our service classes.
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("myService", new MyService());
		// Add registry to CamelContext
		CamelContext context = new DefaultCamelContext(registry);

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION1).to(DESTINATION2);

			}
		});
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION2)
				.to(DESTINATION3)
				.bean(new MyService(),"printText");

			}
		});
		context.start();
		ProducerTemplate producerTemplate = context.createProducerTemplate();
		Date date = new Date();
		producerTemplate.sendBody(DESTINATION1, date);
		ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
		Date date2 = consumerTemplate.receiveBody(DESTINATION2, Date.class);
		System.out.println(date.toString());
		System.out.println(date2.toString());

	}

}
