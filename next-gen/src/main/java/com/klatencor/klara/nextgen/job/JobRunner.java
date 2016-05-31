package com.klatencor.klara.nextgen.job;

import org.apache.log4j.Logger;

import com.klatencor.klara.nextgen.server.metrics.JobMessage;

/**
* 
* A {@link JobRunner} is used to run a {@link Job}.
* It invokes the each phase of a job step by step
* and report the status to certain destination. The {@link Job.State} also will
* be altered. We would like use it as following:
* <p>
* <code>
*  Reporter reporter = new ReporterImpl(); <p>
*  Job job = new JobImpl(); <p>
*  JobRunner jobRunner = new JobRunner(job, reporter);
*  jobRunner.run();
* </code>
* 
* @author jiangzhao
* @date  May 27, 2016
* @version V1.0
*/
public class JobRunner {

	private static final Logger logger = Logger.getLogger(JobRunner.class);
	private Job job;
	private Reporter reporter;
	
	public JobRunner(Job job, Reporter reporter) {
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
		job.setState(Job.State.DONE);
		doReport("all done.");
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
		job.setState(Job.State.SETUP);
		doReport("setup job begins.");
		try {
			job.setup();
			doReport("setup job done.");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			doReport("setup job fails, see logs for details.");
		} 
		return status;
	}
	
	private boolean doExecute() {
		boolean status = true;
		job.setState(Job.State.RUNNING); 
		doReport("run job begins.");
		try {
			job.execute();
			doReport("run job done");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			doReport("run job fails, see logs for details.");
		} 
		return status;
	}
	
	private boolean doClean() {
		boolean status = true;
		job.setState(Job.State.CLEAN);
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
}
