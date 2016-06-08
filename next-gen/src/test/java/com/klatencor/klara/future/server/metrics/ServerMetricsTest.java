package com.klatencor.klara.future.server.metrics;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.job.DefaultReporter;
import com.klatencor.klara.future.job.Job;
import com.klatencor.klara.future.job.JobRunner;
import com.klatencor.klara.future.job.Reporter;

public class ServerMetricsTest {

	static Logger logger = Logger.getLogger(ServerMetricsTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ServerMetrics metrics = new ServerMetrics();
		metrics.startup();
		final Reporter reporter = new DefaultReporter(metrics);
		for (int i = 1; i < 100; ++i) {
			final int index = i;
			new Thread() {
				public void run() {
			SimpleJob job = new SimpleJob("simpleJob", index);
			JobRunner jobRunner = new JobRunner(job, reporter);
			jobRunner.run();
				}
			}.start();
		}
		while(metrics.getUnprocessedMessage() != 0);
		//Thread.sleep(100);
		metrics.shutdown();
		logger.info(metrics.getHistoricalMessages().size());
		logger.info(metrics.getHistoricalMessages());
		
	}
	
	static class SimpleJob extends Job {
		
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

	
}
