package com.example.demo.example3;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class DemoApp {

	private static final String DESTINATION1 = "direct:destination1";
	private static final String DESTINATION2 = "seda:destination2";

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DESTINATION1).process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String inputText = exchange.getIn().getBody(String.class);
						exchange.getOut().setBody(inputText + "\n -Best Wishes from Sankar");
					}
				}).to(DESTINATION2);
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
