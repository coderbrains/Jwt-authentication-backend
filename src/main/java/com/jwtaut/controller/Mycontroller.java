package com.jwtaut.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Mycontroller {

	
	@GetMapping("/welcome")
	public String welcome()
	{
		return "This is not a api . it is just for testing.";
	}
}
