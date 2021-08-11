package com.jwtaut.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtaut.helper.JwtUtil;
import com.jwtaut.services.CustomUserDetailsService;

@Component
public class MyauthenticationFillter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String reqTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;
		String userName = null;

		if (reqTokenHeader != null && reqTokenHeader.startsWith("Bearer")) {
			jwtToken = reqTokenHeader.substring(7);
			try {
				userName = jwtUtil.extractUsername(jwtToken);
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			UserDetails loadUserByUsername = customUserDetailsService.loadUserByUsername(userName);
			
			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loadUserByUsername, null, loadUserByUsername.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}else {
				System.out.println("token is not validated.. ");
			}
			
			
			
		}
		
		filterChain.doFilter(request, response);

	}

}
