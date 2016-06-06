package com.klatencor.klara.future.dal;

// A encapsulation of sql command
public interface DalCommand<T> {

	/**
	 * Construct and run the SQL statement. 
	 * @param executor the SQL executor.
	 * @return the SQL query output.
	 */
	public T run(SqlExecutor executor);
}
