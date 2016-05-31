package com.klatencor.klara.nextgen.test;

import com.klatencor.klara.nextgen.job.Job;

public class SimpleJob extends Job {
	
	public SimpleJob(String jobName, long jobId) {
		this.setJobId(jobId);
		this.setJobName(jobName);
	}

	@Override
	public void setup() throws Exception {
		System.out.println(Thread.currentThread().getName() + ": setup.");
	}

	@Override
	public void execute() throws Exception {
		System.out.println(Thread.currentThread().getName() + ": execute.");
		
	}

	@Override
	public void clean() throws Exception {
		System.out.println(Thread.currentThread().getName() + ": clean.");
		// TODO Auto-generated method stub
		
	}

}
