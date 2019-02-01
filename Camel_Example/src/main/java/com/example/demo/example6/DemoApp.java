package com.example.demo.example6;

import java.util.Date;

import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class DemoApp {

	private static final String DESTINATION1 = "direct:destination1";
	private static final String DESTINATION2 = "activemq:queue:object-queue";
	private static final String DESTINATION3 = "class:com.example.demo.example6.DemoApp?method=printString";

	/**
	 * @param args
	 * @throws Exception
	 * Example of calling a method of class when getting message in MQ using class component
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
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION2).to(DESTINATION3);
				
			}
		});
		context.start();
		ProducerTemplate producerTemplate = context.createProducerTemplate();
		Date  date = new Date();
		producerTemplate.sendBody(DESTINATION1, date);
		ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
		Date  date2 = consumerTemplate.receiveBody(DESTINATION2, Date.class);
		System.out.println(date.toString());
		System.out.println(date2.toString());

	}
	public void printString(Date date){
		System.out.println("Here is the date which I recieved --->"+date.toString());
	}

}
