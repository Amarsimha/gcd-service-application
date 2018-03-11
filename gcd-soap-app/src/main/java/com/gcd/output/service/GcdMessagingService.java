package com.gcd.output.service;

import java.util.Collections;

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
	
	Integer getActiveMessageCount() {
		return 	jmsTemplate.browse(new BrowserCallback<Integer>() {

			@SuppressWarnings("unchecked")
			@Override
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
