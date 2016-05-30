package org.smartframework.datebase.dao;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class SqlExecutor {

	private final static Logger logger = Logger.getLogger(SqlExecutor.class);
	//	private final static String SCAN_PACKAGE = "org.smart.framework.db.dao";

	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate transactionTemplate;
	//	private DataSource ds;
	//	private Map<Class<?>, OrmDao> daoRegistry;

	private ApplicationContext ctx;

	private static SqlExecutor sqlExecutor;
	private AtomicLong txId;

	//	private String scanPackage = SCAN_PACKAGE;

	/**
	 * Singleton
	 */
	private SqlExecutor() {

	}

	public static SqlExecutor getInstance() {
		if (sqlExecutor == null) {
			synchronized(SqlExecutor.class) {
				if (sqlExecutor == null) {
					sqlExecutor = new SqlExecutor();
					try {
						sqlExecutor.init();
					}
					catch(Exception e) {
						logger.error(e.getMessage(), e);
						logger.info("bye bye ...");
						System.exit(-1);
					}
				}
			}	
		}
		return sqlExecutor;
	}

	private void init() {
		logger.info("init is called.");
		ctx = new ClassPathXmlApplicationContext("/org/smartframework/db/dao/spring-config.xml");
		//		ds = ctx.getBean("dataSource", BasicDataSource.class);
		jdbcTemplate = ctx.getBean("jdbcTemplate", JdbcTemplate.class);
		transactionTemplate = ctx.getBean("transactionTemplate", TransactionTemplate.class);
		//		daoRegistry = new HashMap<Class<?>, OrmDao>();
		txId = new AtomicLong();
		//		int daoCount = 0;
		//		// auto-find the dao object
		//		try {
		//			Set<Class<?>> classWithEndNames = ClassUtils.findSubclasses(scanPackage, OrmDao.class);
		//			Iterator<Class<?>> iter = classWithEndNames.iterator();
		//			while(iter.hasNext()) {
		//				Class<?> clazz = iter.next();
		//				int clazzModifier = clazz.getModifiers();
		//				if ((clazzModifier & Modifier.ABSTRACT) != 0) {
		//					try {
		//						Object instance = clazz.newInstance();
		//						Method method = clazz.getMethod("setJdbcTemplate");
		//						method.invoke(instance, jdbcTemplate);
		//						daoRegistry.put(clazz, (OrmDao)instance);
		//						++ daoCount;
		//					} catch (Exception e) {
		//						logger.error(e.getClass(), e);
		//					}
		//				}
		//			}
		//		} catch (IOException e) {
		//			logger.error(e.getMessage(), e);
		//		}
		//		logger.info(String.format("find %d DAO objects under package %s", daoCount, SCAN_PACKAGE));
		//		if (logger.isDebugEnabled()) {
		//			logger.debug(daoRegistry.toString());
		//		}
		logger.info("init ends.");
	}


	public void destory() {
		logger.info("destory is called.");
		ctx = null;
		sqlExecutor = null;
	}

	//	/**
	//	 * Get the corresponding DAO object according to Class {@code clazz}.
	//	 * Null if not found.
	//	 * 
	//	 * @param clazz the class type of the DAO.
	//	 * @return the desired DAO.
	//	 */
	//	public <T> T getDao(Class<T> clazz) {
	//		if (daoRegistry.containsKey(clazz)) {
	//			return (T) daoRegistry.get(clazz);
	//		}
	//		logger.info("can't find dao with class " + clazz);
	//		return null;
	//	}

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
			return command.execute(this);
		} catch(Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
		} finally {

			logger.debug("execute daoCommand ends, txID: " + txId + " status:" + status);

		}
		return null;
	}

	/**
	 * Execute the DaoCommand in transaction mode.
	 * 
	 * @param command the command.
	 * @return the output of the command.
	 */
	public <T> T doTransaction(DaoCommand<T> command) {
		long txId = getTxId();
		boolean status = true;

		logger.info("execute transation daoCommand starts, txID: " + txId);
		try {
			return transactionTemplate.execute(new TransactionCallback<T>(){
				@Override
				public T doInTransaction(TransactionStatus status) {
					return command.execute(SqlExecutor.this);
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

	public long getTxId() {
		return txId.getAndIncrement();
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static <T> T transaction(DaoCommand<T> command) {
		return getInstance().doTransaction(command);
	}

	public static <T> T exectue(DaoCommand<T> command) {
		return getInstance().doExecution(command);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void error(String msg, Throwable t) {
		logger.error(msg, t);
	}
	//	public String getScanPackage() {
	//		return scanPackage;
	//	}
	//
	//	public void setScanPackage(String scanPackage) {
	//		this.scanPackage = scanPackage;
	//	}

}
