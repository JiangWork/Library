package org.smartframework.jobhub.core;

import org.smartframework.jobhub.protocol.JobState;

// used to describe a job status.
public class JobEntry {
	
	private long id;
	
	private JobState state;
	
	private long createdTime;
	
	private long startTime;
	
	private boolean success;
	
	private String reason;
	
	private int progress;
	
	private long lastUpdated;
	
	private JobDefinition definition;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public JobDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(JobDefinition definition) {
		this.definition = definition;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public boolean equals(Object another) {
		return another instanceof JobEntry ? 
				this.id == ((JobEntry)another).id : false;
	}
	
	public void finishSuccess() {
		this.setLastUpdated(System.currentTimeMillis());
		this.setSuccess(true);
		this.setProgress(100);
		this.setState(JobState.DONE);
	}
	
	public void finishFail(String msg) {
		this.setLastUpdated(System.currentTimeMillis());
		this.setSuccess(false);
		this.setReason(msg);
		this.setProgress(100);
		this.setState(JobState.DONE);
	}
	
}
