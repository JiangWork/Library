package org.smartframework.system.job;

public class SimpleReporter implements Reporter {

	@Override
	public void report(Job job, String msg) {
		
		System.out.println(job.getJobId() + " " + job.getState() + " " + msg);
	}

}
