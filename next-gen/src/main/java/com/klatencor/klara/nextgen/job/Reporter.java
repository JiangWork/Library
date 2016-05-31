package com.klatencor.klara.nextgen.job;

import com.klatencor.klara.nextgen.server.metrics.JobMessage;

/**
* 
* A {@link Reporter} is used to report Job status 
* to certain destination.
* 
* @author jiangzhao
* @date  May 27, 2016
* @version V1.0
*/
public interface Reporter {
	
	/**
	 * Report the status of a Job to certain destination.
	 * Also can be logged the information into log system.
	 * 
	 * @param jobMessage a message to be reported.
 	 */
	public void report(JobMessage jobMessage);
	
}
