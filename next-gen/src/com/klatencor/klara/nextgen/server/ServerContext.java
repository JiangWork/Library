package com.klatencor.klara.nextgen.server;

import com.klatencor.klara.nextgen.server.metrics.ServerMetrics;

/**
 * A {@link ServerContext} contains general server information
 * that can be accessed by other classes.
 * 
 * @author jiangzhao
 * @date  May 23, 2016
 * @version V1.0
 */
public class ServerContext {
	
	private String serverName;
	private static ServerContext serverContext;
	private ServerMetrics metrics;

	private ServerContext() {
		metrics = ServerMetrics.getInstance();
	}
	
	public synchronized static  ServerContext getInstance() {
		if (serverContext == null) {
			serverContext = new ServerContext();
		}
		return serverContext;
	}
	
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
	
	
}
