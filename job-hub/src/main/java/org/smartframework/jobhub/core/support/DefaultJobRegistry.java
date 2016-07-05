package org.smartframework.jobhub.core.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.smartframework.jobhub.core.JobEntry;
import org.smartframework.jobhub.core.JobRegistry;
import org.smartframework.jobhub.protocol.JobState;

/**
 * A simple thread-safe implementation of {@link JobRegisty}.
 * It uses Map to store {@link JobEntry}.
 * 
 * @author Miller
 * @date Jul 2, 2016 11:53:00 PM
 */
public class DefaultJobRegistry implements JobRegistry {

	private Map<Long, JobEntry> jobRegistry;
	
	public DefaultJobRegistry() {
		jobRegistry = new ConcurrentHashMap<Long, JobEntry>();
	}
	
	@Override
	public void registerJobEntry(JobEntry entry) {
		jobRegistry.put(entry.getId(), entry);
	}

	@Override
	public JobEntry getJobEntry(long jobId) {
		// TODO Auto-generated method stub
		return jobRegistry.get(jobId);
	}

	@Override
	public JobEntry remove(long jobId) {
		// TODO Auto-generated method stub
		return jobRegistry.remove(jobId);
	}

	@Override
	public Set<JobEntry> getJobEntries(JobState state) {
		Iterator<Map.Entry<Long, JobEntry>> iter = jobRegistry.entrySet().iterator();
		Set<JobEntry> entrySet = new HashSet<JobEntry>();
		while(iter.hasNext()) {
			JobEntry entry = iter.next().getValue();
			if (entry.getState() == state) {
				entrySet.add(entry);
			}
		}
		// TODO Auto-generated method stub
		return entrySet;
	}

	@Override
	public boolean contains(long jobId) {
		// TODO Auto-generated method stub
		return jobRegistry.containsKey(jobId);
	}

}
