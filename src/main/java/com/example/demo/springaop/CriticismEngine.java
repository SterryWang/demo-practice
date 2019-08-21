package com.example.demo.springaop;

import org.springframework.stereotype.Component;

@Component
public class CriticismEngine {
	
	public String   getCriticism() {
		
		return "say something about the performance...";		
	}

}
