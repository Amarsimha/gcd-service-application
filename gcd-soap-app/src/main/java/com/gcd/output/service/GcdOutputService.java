package com.gcd.output.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcd.output.dao.GcdEntityManager;

@Service
public class GcdOutputService {

	private static Logger logger = LoggerFactory.getLogger(GcdOutputService.class);
	
	@Autowired
	private GcdEntityManager gcdEntityManager;
	
	@Autowired
	private GcdMessagingService messagingService;
	
	
	public Integer getGcd() {
		Integer retVal = -1;
		if(messagingService.getActiveMessageCount() >= 2) {
			Integer firstNum = messagingService.readFromQueue();
			Integer secondNum = messagingService.readFromQueue();
			retVal = computeGcd(firstNum, secondNum);
			logger.debug("Gcd of {0}, {1} is {2}", firstNum, secondNum, retVal);
			gcdEntityManager.saveGcd(retVal);
		} else {
			logger.error("Got gcd request...but no message in queue. Going to return -1 instead");
		}
		return retVal;
	}
	
	public Integer computeGcdSum() {
		List<Integer> gcdList = getGcdList();
		return gcdList.stream().mapToInt(Integer::intValue).sum();
	}
	
	public List<Integer> getGcdList() {
		return gcdEntityManager.gcdList().stream().
				map(gcd -> gcd.getValue()).collect(Collectors.toList());
	}
	
	private Integer computeGcd(Integer firstNum, Integer secondNum) {
		if(firstNum == 0) return secondNum;
		if(secondNum == 0) return firstNum;
		
		return computeGcd(secondNum, firstNum % secondNum);
	}
}
