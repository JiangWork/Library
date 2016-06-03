package com.klatencor.klara.future.server.metrics;

public class JobStatistics {
	
	private String jobName;
	
	private int count;
	
	private long totalTime;
	
	private long factor;

	public int incCount(int add) {
		int oldValue = count;
		count += add;
		return oldValue;
	}
	
	public long incTotalTime(long add) {
		long oldValue = totalTime;
		totalTime += add;
		return oldValue;
	}
	
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getFactor() {
		return factor;
	}

	public void setFactor(long factor) {
		this.factor = factor;
	}
	
	public String formartJobStatistics() {
		return String.format("JobName:%s, Count:%d, TotalTime:%d, Factor:%d", 
				jobName, count, totalTime, factor);
	}
}
