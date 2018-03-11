package com.gcd.output.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.gcd.output.service.GcdOutputService;
import com.gcd.output.wsdl.GcdListResponse;
import com.gcd.output.wsdl.GcdListResponse.GcdList;
import com.gcd.output.wsdl.GcdResponse;
import com.gcd.output.wsdl.GcdSumResponse;

@Endpoint
public class GcdEndPoint {
	
	private static final Logger logger = LoggerFactory.getLogger(GcdEndPoint.class);
	
	private static final String NAMESPACE_URI = "http://gcdservice.com/soap";

	@Autowired
	private GcdOutputService gcdOutputService;
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="gcdRequest")
	@ResponsePayload
	public GcdResponse gcd() {
		logger.debug("Got gcd compuation request");
		GcdResponse response = new GcdResponse();
		response.setGcd(gcdOutputService.getGcd());
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="gcdListRequest")
	@ResponsePayload
	public GcdListResponse gcdList() {
		logger.debug("Got gcd list request");
		GcdListResponse gcdListResponse = new GcdListResponse();
		GcdList gcdList = new GcdList();
		gcdList.getGcd().addAll(gcdOutputService.getGcdList());
		gcdListResponse.setGcdList(gcdList);
		return gcdListResponse;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="gcdSumRequest")
	@ResponsePayload
	public GcdSumResponse gcdSum() {
		logger.debug("Got gcd sum request");
		GcdSumResponse response = new GcdSumResponse();
		response.setGcdSum(gcdOutputService.computeGcdSum());
		return response;
	}
}
