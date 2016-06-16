package com.klatencor.klara.future.server.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.klatencor.klara.future.job.JobState;

/**
 * 
 * A {@link JobRecord} records the job status.
 *
 * @author jiangzhao
 * @date May 27, 2016
 * @version V1.0
 */
public class JobRecord {
	
	private long jobId;
	private String jobName;
	private Date startTime;
	private Date endTime;
	private JobState state;
	private List<String> messages;
	
	public JobRecord(long jobId, String jobName) {
		this.jobId = jobId;
		this.jobName = jobName;
		messages = new ArrayList<String>();
		state = JobState.CREATED;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void addMessage(long reportedTime, String message) {
		this.messages.add(String.format("%s %s", new Date(reportedTime), message));
	}
	
	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}
	
	public String formatJobRecord() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("JobId: %d, JobName: %s, StartTime: %s, EndTime: %s, Messages: %d.\n", 
				jobId, jobName, startTime, endTime, messages.size()));
		for (int i = 0; i < messages.size(); ++ i) {
			sb.append(String.format("%d: %s\n", (i+1), messages.get(i)));
		}
		return sb.toString();
	}
	
}
