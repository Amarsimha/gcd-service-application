package com.gcd.input.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gcd.input.model.Gcd;

@Component
public class GcdEntityManager {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveInput(Integer firstNum, Integer secondNum) {
		Gcd gcdInput = new Gcd();
		gcdInput.setFirstNumber(firstNum);
		gcdInput.setSecondNumber(secondNum);
		gcdInput.setValue(-1);
		
		Session session = sessionFactory.getCurrentSession();
		session.save(gcdInput);
	}
	
	@Transactional(readOnly = true)
	public List<Gcd> listInput() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Gcd> criteriaQuery = criteriaBuilder.createQuery(Gcd.class);
		criteriaQuery.select(criteriaQuery.from(Gcd.class));
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	@Transactional(readOnly = true) 
	public List<Gcd> listUnprocessedInput() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Gcd> criteriaQuery = criteriaBuilder.createQuery(Gcd.class);
		Root<Gcd> gcdRoot = criteriaQuery.from(Gcd.class);
		criteriaQuery.select(gcdRoot).where(criteriaBuilder.equal(gcdRoot.get("value"), -1));
		criteriaQuery.orderBy(criteriaBuilder.asc(gcdRoot.get("id")));
		return session.createQuery(criteriaQuery).getResultList();
	}
}
