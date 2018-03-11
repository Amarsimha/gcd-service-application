package com.gcd.input.service;

import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class GcdMessagingService {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	void addInputToQueue(Integer input) {
		jmsTemplate.convertAndSend(input);
	}
	
	void addListToQueue(List<Integer> numbers) {
		for(Integer num : numbers) {
			addInputToQueue(num);
		}
	}

	Integer getActiveMessageCount() {
		return 	jmsTemplate.browse(new BrowserCallback<Integer>() {

			@SuppressWarnings("unchecked")
			public Integer doInJms(Session session, QueueBrowser qBrowser) throws JMSException {
				return Collections.list(qBrowser.getEnumeration()).size();
			}
		});

	}
	
	Integer readFromQueue() {
		Integer number = (Integer) jmsTemplate.receiveAndConvert();
		return number;
	}
}
