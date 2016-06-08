package com.klatencor.klara.future.job;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.server.ServerConfiguration;
import com.klatencor.klara.future.server.metrics.JobMessage;
import com.klatencor.klara.future.server.metrics.ServerMetrics;

/**
 * 
 * {@link DefaultReporter} reports the message to {@link ServerMetrics}
 * and log them down.
 *
 * @author jiangzhao
 * @date May 27, 2016
 * @version V1.0
 */
public class DefaultReporter implements Reporter {

	private static final Logger logger = Logger.getLogger(DefaultReporter.class);
	
	// where should i report to
	private ServerMetrics sm;
	
	public DefaultReporter(ServerMetrics sm) {
		this.sm = sm;
	}
	
	@Override
	public void report(JobMessage jobMsg) {
		if (!ServerConfiguration.SHOULD_REPORT) return;
		logger.info(jobMsg.formatMessage());
		if (sm.isRunning()) sm.put(jobMsg); 
		else logger.info("ServerMetrics is not up.");
	}

}
