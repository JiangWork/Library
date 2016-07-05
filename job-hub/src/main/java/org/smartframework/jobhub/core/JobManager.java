package org.smartframework.jobhub.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.smartframework.jobhub.core.support.DefaultJobRegistry;
import org.smartframework.jobhub.core.support.DefaultScheduler;
import org.smartframework.jobhub.core.support.StateChangedAdapter;
import org.smartframework.jobhub.protocol.JobState;


public class JobManager implements JobRegistry {
	
	private static final Logger logger = Logger.getLogger(JobManager.class);
	
	private JobRegistry jobRegistry;
	private Scheduler scheduler;
	private StateChangedListener defaultListener;
	
	private List<StateChangedListener> listeners;
	
	public JobManager() {
		jobRegistry = new DefaultJobRegistry();
		scheduler = new DefaultScheduler();
		defaultListener = new StateChangedAdapter() {
			public void onSubmit(JobEntry jobEntry) {
				scheduler.schedule(jobEntry);
			}
		};
		listeners = new ArrayList<StateChangedListener>();
	}

	/**
	 * submit the job to {@link Scheduler} to run.
	 * @param jobId
	 */
	public void submit(long jobId) throws JobException {
		
		JobEntry entry = getJobEntry(jobId);
		if (entry == null) {
			throw new JobException("No such job id found:" + jobId);
		}
		
		
		defaultListener.onSubmit(entry);
		for (StateChangedListener listener: listeners) {
			listener.onSubmit(entry);
		}
	}
	
	/**
	 * Add and entry to {@link JobRegistry} when a
	 * new job is request
	 */
	@Override
	public void registerJobEntry(JobEntry entry) {
		jobRegistry.registerJobEntry(entry);
		defaultListener.onPrepare(entry);
		for (StateChangedListener listener: listeners) {
			listener.onPrepare(entry);
		}
	}

	@Override
	public JobEntry getJobEntry(long jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobEntry remove(long jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<JobEntry> getJobEntries(JobState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(long jobId) {
		// TODO Auto-generated method stub
		return false;
	}

}
