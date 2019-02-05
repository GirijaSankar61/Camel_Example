package com.example.demo.example8;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class DemoApp {

	private static final String DESTINATION1 = "file:input?noop=true";
	private static final String DESTINATION2 = "file:destination2?noop=true";
	private static final String DESTINATION3 = "file:destination3?noop=true";
	private static final String DESTINATION4 = "file:output?noop=true";

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION1)
				.choice()
				.when(header("CamelFileName").endsWith(".xml"))
					.to(DESTINATION2)
				.when(header("CamelFileName").endsWith(".txt"))
					.to(DESTINATION3)
				.end()
				.to(DESTINATION4);
			}
		});
		context.start();

		ProducerTemplate producerTemplate = context.createProducerTemplate();
		producerTemplate.sendBody(DESTINATION1, "Hello World!");

		ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
		String text = consumerTemplate.receiveBody(DESTINATION2, String.class);
		System.out.println(text);

	}

}
