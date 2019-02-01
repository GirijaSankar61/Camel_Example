package com.example.demo.example7;

import java.util.Date;

public class MyService {
	
	public void printString(Date date){
		System.out.println("Here is the date which I recieved --->"+date.toString());
	}
	public void printText(Date date){
		System.out.println("Here is the text which I recieved using bean() --->"+date.toString());
	}

}
