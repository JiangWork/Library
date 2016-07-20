package library.database.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DaoCommand<T> {
	
	public T execute(JdbcTemplate jdbcTemplate);
	
}
