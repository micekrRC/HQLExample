package com.journaldev.hibernate.main;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.journaldev.hibernate.model.Employee;
import com.journaldev.hibernate.util.HibernateUtil;

import service.QueryService;

public class HQLExamples {
	
	
	private static SessionFactory sessionFactory = null;
	private static Session session = null;


	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		//Prep work
//		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	//	Session session = sessionFactory.getCurrentSession();
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.getCurrentSession();
		
		
		//Get All Employees
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Employee");
		List<Employee> empList = query.list();
		for(Employee emp : empList){
//			System.out.println("List of Employees::"+emp.getId()+","+emp.getAddress().getCity());
			System.out.println("List of Employees::" +
						emp.getId() + "," +
						emp.getName() + "," +
						emp.getAddress().getCity());
			
		}
		
		//Get Employee with id
		query = session.createQuery("from Employee where id= :id");
		query.setLong("id", 3);
		Employee emp = (Employee) query.uniqueResult();
		System.out.println("Employee Name="+emp.getName()+", City="+emp.getAddress().getCity());
		
		
		//Get Employee with id and name rjm-debug - THIS worked !!!
		// maybe issue when passing Query object by reference to a method
		query = session.createQuery("from Employee where id= :id and name= :name"); // rjm-debug
		query.setLong("id", 4);
		query.setParameter("name", "Jack");  // rjm-debug
		emp = (Employee) query.uniqueResult();
		System.out.println("Employee Name="+emp.getName()+", City="+emp.getAddress().getCity());
		
		// rjm-debug - dynamic query test CASE !!
		long id = 4;
		String name = "Jack";
		String q = "from Employee where id= " + id + " and name= '" + name + "'";		
		query = session.createQuery(q); // rjm-debug
		//query.setLong("id", 4);
		//query.setParameter("name", "Jack");  // rjm-debug
		emp = (Employee) query.uniqueResult();
		System.out.println("Employee Name="+emp.getName()+", City="+emp.getAddress().getCity());
		
		
		Object o = QueryService.getInstance().query(q);
		if (o instanceof Employee) {			
			Employee e = (Employee)o;
			System.out.println("(QueryService) Employee Name="+e.getName()+", City="+e.getAddress().getCity());
		}
		
		
		//Update Employee
		query = session.createQuery("update Employee set name= :name where id= :id");
		query.setParameter("name", "Pankaj Kumar");
		query.setLong("id", 1);
		int result = query.executeUpdate();
		System.out.println("Employee Update Status="+result);

		//Delete Employee, we need to take care of foreign key constraints too
		query = session.createQuery("delete from Address where id= :id");
		query.setLong("id", 4);
		result = query.executeUpdate();
		System.out.println("Address Delete Status="+result);
		
		query = session.createQuery("delete from Employee where id= :id");
		query.setLong("id", 4);
		result = query.executeUpdate();
		System.out.println("Employee Delete Status="+result);
		
		//Aggregate function examples
		query = session.createQuery("select sum(salary) from Employee");
		double sumSalary = (Double) query.uniqueResult();
		System.out.println("Sum of all Salaries= "+sumSalary);
		
		//join examples
		query = session.createQuery("select e.name, a.city from Employee e "
				+ "INNER JOIN e.address a");
		List<Object[]> list = query.list();
		for(Object[] arr : list){
			System.out.println(Arrays.toString(arr));
		}
		
		// rjm-debug
		//someMoreQueries(session,query);
//		someMoreQueries1(session);
		someMoreQueries1();		
		
		
		
		//rolling back to save the test data
		tx.rollback();
		
		//closing hibernate resources
		sessionFactory.close();
	}

	
	private static void someMoreQueries1() {
		/*
		this does NOT work when passing
		Session session, Query query
		by reference  !!!
		
		or just the Session object
		it may also be related to the static modifier;
		
		
		
		must access both of these 
		Session session, Query query
		within the scope/context of where they are creatED		
				
	
		*/
		Query query = session.createQuery("from Employee where id= :id and name= :name"); // rjm-debug
		query.setLong("id", 4);
		query.setParameter("name", "Jack");  // rjm-debug
		Employee emp = (Employee) query.uniqueResult();
		System.out.println("Employee Name="+emp.getName()+", City="+emp.getAddress().getCity());

	}
	
	
	private static void someMoreQueries(Session session, Query query) {
		
		long id = 4;
		String name = "Jack";
		
		//query = session.createQuery("from Employee where id= :id");
		//query = session.createQuery("from Employee where id= :id and name= :name");
		// use static values
		
		//query = session.createQuery("from Employee where id= 4 and name= 'Jack'"); // not working, getting NULL
//		query = session.createQuery("from Employee where Employee.id= 4 and Employee.name= 'Jack'"); // not valid		
		
		query = session.createQuery("from Employee where id= ? and name= ?"); // not working
		
		query.setLong(0, id);
		query.setParameter(1, name);		
		
		/*
		query.setLong("id", id);
		//query.setString("name", name); did NOT work
		query.setParameter("name", name);
		
http://www.javawebtutor.com/articles/hibernate/hibernate_query_language_hql_example.php		
		String hql = "from Stock s where s.stockCode = ? and s.stockName = ?";
List result = session.createQuery(hql)
.setString(0, "1234")
.setParameter(1, "HUL")
.list();

		String qString = query.getQueryString();
		System.out.println("qString: " + qString);
		String [] nParmas = query.getNamedParameters();
		System.out.println("getNamedParameters: " + nParmas);
		
		
		*/
			

/*
		query = session.createQuery("from Employee where id= :id");
		query.setLong("id", 3);
		Employee emp = (Employee) query.uniqueResult();
*/
		
		
		//Object o = query.uniqueResult();
		
		Employee emp = (Employee) query.uniqueResult();
		
		if (emp == null) {
			
			System.out.println("id: " + id +
					" name: " + name +
					" NOT FOUND!");
			
		} else {
		
		System.out.println("someMoreQueries() query by id and name Employee Name = "+
		  emp.getName() +
		  ", City=" +
		  emp.getAddress().getCity());
		
		}	

		
		
	}
	
}
