package com.klatencor.klara.nextgen.job;

import org.apache.log4j.Logger;

import com.klatencor.klara.nextgen.server.ServerConfiguration;
import com.klatencor.klara.nextgen.server.ServerContext;
import com.klatencor.klara.nextgen.server.metrics.JobMessage;
import com.klatencor.klara.nextgen.server.metrics.ServerMetrics;

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
	
	private ServerContext sc;
	
	public DefaultReporter(ServerContext sc) {
		this.sc = sc;
	}
	
	@Override
	public void report(JobMessage jobMsg) {
		if (!ServerConfiguration.SHOULD_REPORT) return;
		logger.info(jobMsg.formatMessage());
		ServerMetrics sm = sc.getMetrics();
		if (sm.isRunning()) sm.put(jobMsg); 
		else logger.info("ServerMetrics is not up.");
	}

}
