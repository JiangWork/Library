package org.smartframework.system.job;

public class JobTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Job job = new SimpleJob("simpleJob", 1L);
		JobRunner runner = new JobRunner(job, new SimpleReporter());
		runner.run();
	}

}
