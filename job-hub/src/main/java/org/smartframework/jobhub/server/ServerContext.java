package org.smartframework.jobhub.server;

import org.smartframework.jobhub.core.JobManager;

/**
 * A {@link ServerContext} contains general server information that can be
 * accessed by other classes.
 * 
 * @author Miller
 * @date Jul 2, 2016 9:53:53 PM
 */
public class ServerContext {
	
	public final static int CLIENT_PROTOCOL_PORT = 32101;
	public final static int INNER_PROTOCOL_PORT = 32102;
	public final static int UPLOAD_PROTOCOL_PORT = 32103;
	
	
	private JobServer jobServer;
	private JobManager jobManager;
	private UploadServer uploadServer;
	private ClientProtocolServer clientProtocolServer;
	
	private int clientProtocolPort;
	private int innerProtocolPort;
	private int uploadServerPort;
	
	
	public JobServer getJobServer() {
		return jobServer;
	}
	public void setJobServer(JobServer jobServer) {
		this.jobServer = jobServer;
	}
	public int getClientProtocolPort() {
		return clientProtocolPort;
	}
	public void setClientProtocolPort(int clientProtocolPort) {
		this.clientProtocolPort = clientProtocolPort;
	}
	public int getInnerProtocolPort() {
		return innerProtocolPort;
	}
	public void setInnerProtocolPort(int innerProtocolPort) {
		this.innerProtocolPort = innerProtocolPort;
	}
	public int getUploadServerPort() {
		return uploadServerPort;
	}
	public void setUploadServerPort(int uploadServerPort) {
		this.uploadServerPort = uploadServerPort;
	}
	public UploadServer getUploadServer() {
		return uploadServer;
	}
	public void setUploadServer(UploadServer uploadServer) {
		this.uploadServer = uploadServer;
	}
	public ClientProtocolServer getClientProtocolServer() {
		return clientProtocolServer;
	}
	public void setClientProtocolServer(ClientProtocolServer clientProtocolServer) {
		this.clientProtocolServer = clientProtocolServer;
	}
	public JobManager getJobManager() {
		return jobManager;
	}
	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}
	
	
}
