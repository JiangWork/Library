package com.klatencor.klara.future.job;

/**
* 
* The job definition: includes three different methods.
* 
* @author jiangzhao
* @date  May 23, 2016
* @version V1.0
*/
public interface JobDefinition {
	
	/**
	 * Set the information needed by this job.
	 * @param parameters
	 */
	public void setParameters(JobParameters parameters);
	
	/**
	 * Setup the running environment.
	 * 
	 * @throws Exception
	 */
	public void setup() throws Exception;
	
	/**
	 * Execue the job.
	 * @throws Exception
	 */
	public void execute() throws Exception;
	
	/**
	 * Clean the job, like temporary files.
	 * @throws Exception
	 */
	public void clean() throws Exception;
	
}
