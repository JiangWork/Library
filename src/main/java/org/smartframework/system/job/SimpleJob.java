package org.smartframework.system.job;

public class SimpleJob extends Job{

	public SimpleJob(String jobName, long jobId) {
		setJobName(jobName);
		setJobId(jobId);
	}
	
	@Override
	public void setup() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("setup");
		throw new Exception("e");
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		System.out.println("execute");
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		System.out.println("clean");
	}

}
