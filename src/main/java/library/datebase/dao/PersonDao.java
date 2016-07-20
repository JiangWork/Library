package library.datebase.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class PersonDao {

	public static Person get(final int id) {
		return SqlExecutor.exectue(new DaoCommand<Person>() {
			@Override
			public Person execute(SqlExecutor executor) {
				// TODO Auto-generated method stub
				String sql = "select * from t_person where id=?";
				executor.debug("SQL:" + sql);
				return (Person) executor.getJdbcTemplate().query(sql, new Object[]{id},  
						new BeanPropertyRowMapper<Person>(Person.class)).get(0);
			}
			
		});
	}
	
	public static void insert(final Person person) {
		SqlExecutor.exectue(new DaoCommand<Void>() {

			@Override
			public Void execute(SqlExecutor executor) {
				String sql = "insert into t_person (name, gender) values(?, ?)";
				executor.debug("SQL: " + sql);
				executor.getJdbcTemplate().update(sql, person.getName(), person.getGender());
				return null;
			}
			
		});
	}

	public static void transaction() {
		SqlExecutor.transaction(new DaoCommand<Void>() {

			@Override
			public Void execute(SqlExecutor executor) {
				Person p = new Person();
				p.setName("eva");
				p.setGender("female");
				PersonDao.insert(p);
				p.setName("eva2");
				String sql = "insert into t_person (name, gender) values(?, ?)";
				executor.debug("SQL: " + sql);
				executor.getJdbcTemplate().update(sql, p.getName(), p.getGender());
				return null;
				
			}
			
		});
	}
	
	
}
