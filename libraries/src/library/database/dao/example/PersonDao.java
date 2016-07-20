package library.database.dao.example;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import library.database.dao.DaoCommand;
import library.database.dao.SQLExecutor;

public class PersonDao {

	private SQLExecutor executor;
	
	public Person get(final int id) {
		return executor.doExecution(new DaoCommand<Person>() {
			@Override
			public Person execute(JdbcTemplate jdbcTemplate) {
				String sql = "select * from t_person where id=?";
				return (Person) jdbcTemplate.query(sql, new Object[]{id},  
						new BeanPropertyRowMapper<Person>(Person.class)).get(0);
			}
			
		});
	}
	
	public void insert(final Person person) {
		executor.doExecution(new DaoCommand<Void>() {
			@Override
			public Void execute(JdbcTemplate jdbcTemplate) {
				String sql = "insert into t_person (name, gender) values(?, ?)";
				jdbcTemplate.update(sql, person.getName(), person.getGender());
				return null;
			}
			
		});
	}

//	public static void transaction() {
//		SQLExecutor.transaction(new DaoCommand<Void>() {
//
//			@Override
//			public Void execute(SQLExecutor executor) {
//				Person p = new Person();
//				p.setName("eva");
//				p.setGender("female");
//				PersonDao.insert(p);
//				p.setName("eva2");
//				String sql = "insert into t_person (name, gender) values(?, ?)";
//				executor.debug("SQL: " + sql);
//				executor.getJdbcTemplate().update(sql, p.getName(), p.getGender());
//				return null;
//				
//			}
//			
//		});
//	}
	
	
}
