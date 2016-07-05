package org.smartframework.jobhub.server;

import org.smartframework.jobhub.core.JobManager;

/**
 * A job server provide job execution services.
 * 
 * @author Miller
 * @date Jul 2, 2016 9:57:46 PM
 */
public class JobServer {
	
	public final static int INNER_PROTOCOL_PORT = 32102;
	
	private JobManager jobManager;
	private UploadServer uploadServer;
	private ClientProtocolServer clientProtocolServer;
	
	// create working directory
	
	
	public JobManager getJobManager() {
		return jobManager;
	}
	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
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
	
	
	

}
