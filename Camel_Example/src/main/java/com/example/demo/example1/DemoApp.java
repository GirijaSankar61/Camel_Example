package com.example.demo.example1;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class DemoApp {
	
	public static void main(String[] args) throws Exception {
		CamelContext context=new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				//noop=true is not to delete file from input folder
				//by default it is deleted
				from("file:input?noop=true")
				.to("file:output");
				
			}
		});
		
		while(true)
			context.start();
	}

}
