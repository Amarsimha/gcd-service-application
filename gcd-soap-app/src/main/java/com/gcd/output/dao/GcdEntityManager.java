package com.gcd.output.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gcd.output.model.Gcd;

@Component
public class GcdEntityManager {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveGcd(Integer value) {
		//get first entry in gcd table with gcd value -1
		Gcd gcd = getFirstUncomputedGcdEntry();
		gcd.setValue(value);
		Session session = sessionFactory.getCurrentSession();
		session.save(gcd);
	}
	
	@Transactional(readOnly = true)
	public List<Gcd> gcdList() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Gcd> criteriaQuery = criteriaBuilder.createQuery(Gcd.class);
		Root<Gcd> gcdRoot = criteriaQuery.from(Gcd.class);
		criteriaQuery.select(gcdRoot).where(criteriaBuilder.notEqual(gcdRoot.get("value"), -1));
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	@Transactional
	public Gcd getFirstUncomputedGcdEntry() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Gcd> criteriaQuery = criteriaBuilder.createQuery(Gcd.class);
		Root<Gcd> gcdRoot = criteriaQuery.from(Gcd.class);
		criteriaQuery.select(gcdRoot).where(criteriaBuilder.equal(gcdRoot.get("value"), -1));
		criteriaQuery.orderBy(criteriaBuilder.asc(gcdRoot.get("id")));
		return session.createQuery(criteriaQuery).getResultList().get(0);
	}

}
