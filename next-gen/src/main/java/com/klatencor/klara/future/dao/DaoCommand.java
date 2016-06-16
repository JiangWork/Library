package com.klatencor.klara.future.dao;

import org.springframework.jdbc.core.JdbcTemplate;

// A encapsulation of SQL command
public interface DaoCommand<T> {

	/**
	 * Construct and run the SQL statement. 
	 * @param jdbcTemplate encapsulation of SQL statement execution.
	 * @return the SQL execution result.
	 */
	public T run(JdbcTemplate jdbcTemplate);
}
