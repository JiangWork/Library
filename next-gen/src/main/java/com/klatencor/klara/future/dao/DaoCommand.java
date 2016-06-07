package com.klatencor.klara.future.dao;

// A encapsulation of SQL command
public interface DaoCommand<T> {

	/**
	 * Construct and run the SQL statement. 
	 * @param executor the SQL executor.
	 * @return the SQL query output.
	 */
	public T run(SQLExecutor executor);
}
