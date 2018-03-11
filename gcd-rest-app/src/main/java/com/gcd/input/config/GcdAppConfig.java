package com.gcd.input.config;

import static org.hibernate.cfg.AvailableSettings.*;

import java.util.Properties;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = "com.gcd.input")
public class GcdAppConfig {

	private static Logger logger = LoggerFactory.getLogger(GcdAppConfig.class);
	
	private static final String BROKER_URL = "tcp://localhost:61616";
	
	private static final String JMS_QUEUE = "gcdQueue";
	
	@Autowired
	private Environment env;
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		logger.debug("Going to create Hibernate Session Factory");
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		Properties props = new Properties();

		props.put(DRIVER, env.getProperty("mysql.driver"));
	    props.put(URL, env.getProperty("mysql.url"));
	    props.put(USER, env.getProperty("mysql.user"));
	    props.put(PASS, env.getProperty("mysql.password"));
	    
		props.put(DIALECT, env.getProperty("hibernate.dialect"));
		props.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
		
		props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
	    props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
	    props.put(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
	    props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
	    props.put(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));

		sessionFactory.setHibernateProperties(props);
		sessionFactory.setPackagesToScan("com.gcd.input.model");
		
		logger.debug("SessionFactory created");
		return sessionFactory;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		logger.debug("TxManager created");
		return transactionManager;
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(BROKER_URL);
		connectionFactory.setTrustAllPackages(true);
		logger.debug("JMS Connection Factory created");
		return connectionFactory;
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		jmsTemplate.setDefaultDestinationName(JMS_QUEUE);
		logger.debug("JMSTemplate created");
		return jmsTemplate;
	}

}
