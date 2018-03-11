package com.gcd.output.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class SoapAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(SoapAuthenticationEntryPoint.class);
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response, 
			AuthenticationException authException) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

		logger.error("Authentication error detected");
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 : " + authException.getMessage());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName(GcdAppSecurityConfig.REALM);
		super.afterPropertiesSet();
	}
}
