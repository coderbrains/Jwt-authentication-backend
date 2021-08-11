package com.jwtaut.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtaut.helper.JwtUtil;
import com.jwtaut.model.JwtModel;
import com.jwtaut.model.Tokens;
import com.jwtaut.services.CustomUserDetailsService;

@RestController
public class JwtController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService cuService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/token")
	public ResponseEntity<?> generateToken(@RequestBody JwtModel jwtModel)
	{
		System.out.println(jwtModel);

		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtModel.getUserName(), jwtModel.getPassword()));
			
		} catch (UsernameNotFoundException e) {
			
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("you are not authenticated");
		}
	
		UserDetails loadUserByUsername = this.cuService.loadUserByUsername(jwtModel.getUserName());
		
		String generateToken = jwtUtil.generateToken(loadUserByUsername);
		
		Tokens tokens = new Tokens();
		
		tokens.setToken(generateToken);
		
		System.out.println(tokens);
		
		return ResponseEntity.ok(generateToken);
	}
}
