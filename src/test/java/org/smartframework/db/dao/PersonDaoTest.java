package org.smartframework.db.dao;

import org.smartframework.datebase.dao.Person;
import org.smartframework.datebase.dao.PersonDao;

public class PersonDaoTest {

	public static void main(String[] args) {
		Person p = new Person();
		p.setName("jiang");
		p.setGender("male");
		PersonDao.insert(p);
		Person pe = PersonDao.get(1);
		System.out.println(pe.getName());
		System.out.println(pe.getGender());
		PersonDao.transaction();

	}

}
