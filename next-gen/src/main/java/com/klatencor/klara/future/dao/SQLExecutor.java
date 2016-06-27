package com.klatencor.klara.future.dao;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


public class SQLExecutor {
	
	private static final Logger logger = Logger.getLogger(SQLExecutor.class);
	
	private AtomicLong txCounter;
	
	private JdbcTemplate jdbcTemplate;
	
	private TransactionTemplate transactionTemplate;
	
	public SQLExecutor() {
		txCounter = new AtomicLong();
	}
	
	public long getTxId() {
		return txCounter.getAndIncrement();
	}

	public AtomicLong getTxCounter() {
		return txCounter;
	}
	
	public void setTxCounter(AtomicLong txCounter) {
		this.txCounter = txCounter;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	/**
	 * Execute the DaoCommand in transaction mode.
	 * 
	 * @param command the command.
	 * @return the output of the command.
	 */
	public <T> T doTransaction(final DaoCommand<T> command) {
		long txId = getTxId();
		boolean status = true;

		logger.info("execute transation daoCommand starts, txID: " + txId);
		try {
			return transactionTemplate.execute(new TransactionCallback<T>(){
				@Override
				public T doInTransaction(TransactionStatus status) {
					return command.run(getJdbcTemplate());
				}
			});
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			status = false;
		} finally {
			logger.info("execute transation daoCommand ends, txID: " + txId + " status:" + status);
		}
		return null;
	}
	
	/**
	 * Execute the DaoCommand in auto-commit mode.
	 * 
	 * @param command the command.
	 * @return the output of the command.
	 */
	public <T> T doExecution(DaoCommand<T> command) {
		long txId = getTxId();
		boolean status = true;

		logger.info("execute daoCommand starts, txID: " + txId);
		try{
			return command.run(getJdbcTemplate());
		} catch(Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("execute daoCommand ends, txID: " + txId + " status:" + status);

		}
		return null;
	}
	
	public void execute(final String sql) {
		doExecution(new DaoCommand<Void>() {
			@Override
			public Void run(JdbcTemplate jdbcTemplate) {
				jdbcTemplate.execute(sql);
				return null;
			}
			
		});
	}
	
	

}
