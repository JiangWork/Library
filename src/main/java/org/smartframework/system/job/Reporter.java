package org.smartframework.system.job;

public interface Reporter {
	
	/**
	 * Report the status of a Job to somebody.
	 * Also can be logged the information into log system.
	 * 
	 * @param job the job needs to be reported.
	 * @param msg the message.
 	 */
	public void report(Job job, String msg);
	
}
