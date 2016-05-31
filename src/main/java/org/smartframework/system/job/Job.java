package org.smartframework.system.job;

/**
 * Define a job.
 * @author Miller
 * @date May 26, 2016 10:13:16 PM
 */
public abstract class Job {
	/**the job name**/
	private String jobName;
	/**the job Id**/
	private long jobId;
	/**report the job status.**/
	private Reporter reporter;
	/**the configuration of this job**/
	private JobConf conf;
	
	private State state = State.NEW;
	
	public abstract void setup() throws Exception;
	
	public abstract void execute() throws Exception;
	
	public abstract void clean() throws Exception;

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

	public JobConf getConf() {
		return conf;
	}

	public void setConf(JobConf conf) {
		this.conf = conf;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}



	public static enum State {
		NEW("new"),
		RUNNING("running"),
		CLEAN("clean"),
		DONE("done");
		
		String val;
		
		State(String val) {
			this.val = val;
		}
	}
	
}