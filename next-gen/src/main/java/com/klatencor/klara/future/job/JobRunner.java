package com.klatencor.klara.future.job;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.server.metrics.JobMessage;
import com.klatencor.klara.future.thrift.common.Response;

/**
* 
* A {@link JobRunner} is used to run a {@link Job}.
* It invokes the each phase of a job step by step
* and report the status to certain destination. The {@link JobState} also will
* be altered. We would like use it as following:
* <p>
* <code>
*  Reporter reporter = new ReporterImpl(); <p>
*  Job job = new JobImpl(); <p>
*  JobRunner jobRunner = new JobRunner(job, reporter); <p>
*  jobRunner.run(); <p>
*  jobRunner.getResult(); <p>
*  or jobRunner.getResponse();
* </code>
* 
* @author jiangzhao
* @date  May 27, 2016
* @version V1.0
*/
public class JobRunner {

	private static final Logger logger = Logger.getLogger(JobRunner.class);
	private DefaultJob job;
	private Reporter reporter;
	
	public JobRunner(DefaultJob job, Reporter reporter) {
		this.job = job;
		this.reporter = reporter;
	}
	
	public void run() {		
		if (job.getReporter() == null) {
			job.setReporter(reporter);
		}
		
		if (!doSetup() || !doExecute()) {
			// left on purpose
		}
		doClean();
		job.setState(JobState.DONE);
		JobResult result = job.getJobResult();
		if (result.isStatus()) {
			doReport("all done, job status: successful.");
		} else {
			doReport("all done, job status: fail, reason:" + result.getReason() + ".");
		}
	}
	
	private void doReport(String message) {
		JobMessage jobMsg = new JobMessage(
				System.currentTimeMillis(),
				job.getJobId(),
				job.getJobName(),
				job.getState(),
				message);
		reporter.report(jobMsg);
	}
	
	private boolean doSetup() {
		boolean status = true;
		job.setState(JobState.SETUP);
		doReport("setup job begins.");
		try {
			job.setup();
			doReport("setup job done.");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			doReport("setup job fails, see logs for details.");
			job.getJobResult().setStatus(false);
			job.getJobResult().setReason(e.getMessage());
		} 
		return status;
	}
	
	private boolean doExecute() {
		boolean status = true;
		job.setState(JobState.RUNNING); 
		doReport("run job begins.");
		try {
			job.execute();
			doReport("run job done");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			doReport("run job fails, see logs for details.");
			job.getJobResult().setStatus(false);
			job.getJobResult().setReason(e.getMessage());
		} 
		return status;
	}
	
	private boolean doClean() {
		boolean status = true;
		job.setState(JobState.CLEAN);
		doReport("clean job begins");
		try {
			job.clean();
			doReport("clean job done");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			doReport("clean job fails, see logs for details.");
		}
		return status;
	}
	
	/**
	 * Get job processed result.
	 * @return the job result.
	 */
	public JobResult getJobResult() {
		return job.getJobResult();
	}
	
	/**
	 * Package the {@link JobResult} into {@link Response}
	 * which will be later send back to the caller via Thrift.
	 * @return a response.
	 */
	public Response getResponse() {
		return job.getJobResult().createResponse();
	}
}
