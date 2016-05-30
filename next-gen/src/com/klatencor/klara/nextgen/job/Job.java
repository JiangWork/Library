package com.klatencor.klara.nextgen.job;

import com.klatencor.klara.nextgen.server.ServerContext;

/**
* 
* The job definition.
* 
* @author jiangzhao
* @date  May 23, 2016
* @version V1.0
*/
public abstract class Job {
	/**the job name**/
	private String jobName;
	
	/**the job Id**/
	private long jobId;
	
	/**report the job status.**/
	private Reporter reporter;

	/**The {@link serverContext} to provide information**/
	private ServerContext sc;
	
	/**The state of this job.**/
	private State state = State.CREATED;
	
	public abstract void setup() throws Exception;
	
	public abstract void execute() throws Exception;
	
	public abstract void clean() throws Exception;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public Reporter getReporter() {
		return reporter;
	}

	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}
	
	public ServerContext getSc() {
		return sc;
	}

	public void setSc(ServerContext sc) {
		this.sc = sc;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public static enum State {
		CREATED("created"),
		SETUP("setup"),
		RUNNING("running"),
		CLEAN("clean"),
		DONE("done");
		
		String val;
		
		State(String val) {
			this.val = val;
		}
	}
	
}
