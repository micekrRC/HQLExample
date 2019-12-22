package service;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.journaldev.hibernate.util.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.journaldev.hibernate.model.Employee;
import com.journaldev.hibernate.util.HibernateUtil;



public class QueryService {
	
	private SessionFactory sessionFactory = null;
	private Session session = null;	
	
	private static QueryService queryService = null;
	
	public static QueryService getInstance() {		
		if (queryService == null) {			
			queryService = new QueryService();			
		}		
		return queryService;
	}	
	
	public QueryService() {		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.getCurrentSession();
	}
	
	public Object query(String q) {
		
		Object r = null;
		
		Query query = session.createQuery(q);		
		r =  query.uniqueResult();
		
		return r;	
		
	}	
	

}
