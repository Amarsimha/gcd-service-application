package com.gcd.input.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcd.input.dao.GcdEntityManager;
import com.gcd.input.model.Gcd;
import com.google.common.collect.Lists;

@Service
public class GcdInputService {

	private static Logger logger = LoggerFactory.getLogger(GcdInputService.class);
	@Autowired
	private GcdEntityManager gcdEntityManager;
	
	@Autowired
	private GcdMessagingService messagingService;
	
	public void saveInput(final Integer firstNumber, final Integer secondNumber) {
		logger.debug("Saving input and adding to JMS queue");
		gcdEntityManager.saveInput(firstNumber, secondNumber);
		messagingService.addInputToQueue(firstNumber);
		messagingService.addInputToQueue(secondNumber);
	}
	
	public List<Integer> getAllNumbers() {
		List<Gcd> gcdInputList = gcdEntityManager.listInput();
		
		List<Integer> outputList = Lists.newArrayList();
		
		for(Gcd gcdInput : gcdInputList) {
			outputList.add(gcdInput.getFirstNumber());
			outputList.add(gcdInput.getSecondNumber());
		}
		return outputList;
	}
	
	
	@PostConstruct
	public void addAllNumbersToQueue() {

		//Clear all messages in messageQueue
		int msgCount = messagingService.getActiveMessageCount();
		logger.debug("Active message count {} at startup", msgCount);
		while (msgCount > 0) {
			messagingService.readFromQueue();
			msgCount--;
		}

		logger.debug("Cleared all existing messages");
		
		List<Gcd> gcdInputList = gcdEntityManager.listUnprocessedInput();
		
		List<Integer> outputList = Lists.newArrayList();
		
		for(Gcd gcdInput : gcdInputList) {
			outputList.add(gcdInput.getFirstNumber());
			outputList.add(gcdInput.getSecondNumber());
		}
		messagingService.addListToQueue(outputList);
		msgCount = messagingService.getActiveMessageCount();
		logger.debug("Added {} numbers to queue", msgCount);
	}
}
