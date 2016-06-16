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
	 * Get the job processed result.
	 * @return a {@link JobResult}.
	 */
	public JobResult getJobResult();
	
	/**
	 * Setup the running environment.
	 * 
	 * @throws Exception
	 */
	public void setup() throws Exception;
	
	/**
	 * Execute the job.
	 * @throws Exception
	 */
	public void execute() throws Exception;
	
	/**
	 * Clean the job, like temporary files.
	 * Always will be invoked even {@code setup}
	 * or {@code execute} is not successful.
	 * @throws Exception
	 */
	public void clean() throws Exception;
	
}
