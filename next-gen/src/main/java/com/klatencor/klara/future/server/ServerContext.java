package com.klatencor.klara.future.server;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.klatencor.klara.future.dao.SQLExecutor;
import com.klatencor.klara.future.server.metrics.ServerMetrics;

/**
 * A {@link ServerContext} contains general server information that can be
 * accessed by other classes.
 * 
 * @author jiangzhao
 * @date May 23, 2016
 * @version V1.0
 */
public class ServerContext {

	private BeanFactory beanFactory;

	// *** For test ***//
	private String serverName;
	private ServerMetrics metrics;
	private SQLExecutor sqlExecutor;
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public ServerContext() {

	}

	public DataSource getDataSource() {
		return beanFactory.getBean("dataSource", DataSource.class);
	}

	public String getServerName() {
		return beanFactory.getBean("serverName", String.class);
	}

	public ServerMetrics getMetrics() {
		return beanFactory.getBean("metrics", ServerMetrics.class);
	}

	public SQLExecutor getSqlExecutor() {
		return beanFactory.getBean("sqlExecutor", SQLExecutor.class);
	}

	public JdbcTemplate getJdbcTemplate() {
		return beanFactory.getBean("jdbcTemplate", JdbcTemplate.class);
	}

	public void setSqlExecutor(SQLExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setMetrics(ServerMetrics metrics) {
		this.metrics = metrics;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
