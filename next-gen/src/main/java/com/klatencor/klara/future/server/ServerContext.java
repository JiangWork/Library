package com.klatencor.klara.future.server;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.klatencor.klara.future.dao.SQLExecutor;
import com.klatencor.klara.future.job.Reporter;
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

	private String serverName;
	private ServerMetrics metrics;
	private SQLExecutor sqlExecutor;
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private Reporter reporter;
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public ServerMetrics getMetrics() {
		return metrics;
	}
	public void setMetrics(ServerMetrics metrics) {
		this.metrics = metrics;
	}
	public SQLExecutor getSqlExecutor() {
		return sqlExecutor;
	}
	public void setSqlExecutor(SQLExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public Reporter getReporter() {
		return reporter;
	}
	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}
}
