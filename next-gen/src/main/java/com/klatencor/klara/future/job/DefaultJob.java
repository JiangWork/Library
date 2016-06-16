package com.klatencor.klara.future.job;

import com.klatencor.klara.future.server.Server;
import com.klatencor.klara.future.server.metrics.JobMessage;

/**
 * A default implementation of {@link ReportableJob}
 * 
 * @author jiangzhao
 * @date  Jun 14, 2016
 * @version V1.0
 */
public class DefaultJob implements ReportableJob {
	
	/**the job name**/
	private String jobName;
	
	/**the job Id**/
	private long jobId;
	
	/**report the job status.**/
	private Reporter reporter;
	
	/**The state of this job.**/
	private JobState state = JobState.CREATED;	
	
	/**the parameters of this job**/
	private JobParameters parameters;
	
	/**the processed result**/
	private JobResult result;
	
	public DefaultJob(long jobId, String jobName, Reporter reporter) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.reporter = reporter;
		result = new JobResult();
	}

	/**
	 * Need to be overrided in sub-class.
	 */
	@Override
	public void setup() throws Exception {
		System.out.println(this.getClass().getCanonicalName() + ": setup.");	
	}

	/**
	 * Need to be overrided in sub-class.
	 */
	@Override
	public void execute() throws Exception {
		System.out.println(this.getClass().getCanonicalName() + ": execute.");			
	}

	/**
	 * Need to be overrided in sub-class.
	 */
	@Override
	public void clean() throws Exception {
		System.out.println(this.getClass().getCanonicalName() + ": clean.");	
		
	}

	@Override
	public void report(String message) {
		JobMessage jobMsg = new JobMessage(
				System.currentTimeMillis(),
				getJobId(),
				getJobName(),
				getState(),
				message);
		if (reporter != null) {
			reporter.report(jobMsg); 
		}
		else {
			Server.getContext().getReporter().report(jobMsg);
		}		
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public Reporter getReporter() {
		return reporter;
	}

	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}

	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}

	@Override
	public void setParameters(JobParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public JobResult getJobResult() {
		return result;
	}
	
	public void setJobResult(JobResult result) {
		this.result = result;
	}
	
	public JobParameters getParamters() {
		return parameters;
	}
	

}
