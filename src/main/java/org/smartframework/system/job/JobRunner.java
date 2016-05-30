package org.smartframework.system.job;

import org.apache.log4j.Logger;

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
			// left
		}
		doClean();
		job.setState(Job.State.DONE);
		reporter.report(job, "all done.");
	}
	
	private boolean doSetup() {
		boolean status = true;
		job.setState(Job.State.NEW);
		reporter.report(job, "setup job begins.");
		try {
			job.setup();
			reporter.report(job, "setup job done.");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			reporter.report(job, "setup job fails, see logs for details.");
		} 
		return status;
	}
	
	private boolean doExecute() {
		boolean status = true;
		job.setState(Job.State.RUNNING);
		reporter.report(job, "run job begins.");
		try {
			job.execute();
			reporter.report(job, "run job done");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			reporter.report(job, "run job fails, see logs for details.");
		} 
		return status;
	}
	
	private boolean doClean() {
		boolean status = true;
		job.setState(Job.State.CLEAN);
		reporter.report(job, "clean job begins");
		try {
			job.clean();
			reporter.report(job, "clean job done");
		} catch (Exception e) {
			status = false;
			logger.error(e.getMessage(), e);
			reporter.report(job, "clean job fails, see logs for details.");
		}
		return status;
	}
}
