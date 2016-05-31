package com.klatencor.klara.nextgen.test;

import org.apache.log4j.Logger;

import com.klatencor.klara.nextgen.job.DefaultReporter;
import com.klatencor.klara.nextgen.job.JobRunner;
import com.klatencor.klara.nextgen.job.Reporter;
import com.klatencor.klara.nextgen.server.ServerContext;
import com.klatencor.klara.nextgen.server.metrics.ServerMetrics;

public class ServerMetricsTest {

	static Logger logger = Logger.getLogger(ServerMetricsTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ServerMetrics metrics = ServerMetrics.getInstance();
		metrics.startup();
		Reporter reporter = new DefaultReporter(ServerContext.getInstance());
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
	
}
