package com.klatencor.klara.future.server;

import com.klatencor.klara.future.server.metrics.ServerMetrics;

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
	private ServerMetrics metrics;
	

	public ServerContext() {
	
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
