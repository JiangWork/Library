package com.klatencor.klara.nextgen.server.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.klatencor.klara.nextgen.job.Job;
import com.klatencor.klara.nextgen.server.ServerConstants;

/**
 * Provide statistical status of current running server. 
 * 
 * @author jiangzhao
 * @date  May 23, 2016
 * @version V1.0
 */
public class ServerMetrics {

	private static final Logger logger = Logger.getLogger(ServerMetrics.class);
	
	/**The startup time of this server**/
	private Date startupTime;
	
	/**A queue to store incoming message**/
	private BlockingQueue<JobMessage> messageQueue;
	
	/**A worker to consume message in the queue**/
	private JobMessageWorker messageWorker;
	
	/**Keeps the latest 50 messages**/
	private List<String> historicalMessages;
	
	/**keep current running jobs**/
	private Map<Long, JobRecord> runningJobs;
	
	/** <jobName, statistics> **/
	private Map<String, JobStatistics> jobStats;
	
	private static ServerMetrics sm;
	
	private ServerMetrics() {
		startupTime = new Date(System.currentTimeMillis());
		messageQueue = new LinkedBlockingQueue<JobMessage>();
		messageWorker = new JobMessageWorker();
		historicalMessages = new ArrayList<String>();
		runningJobs = new HashMap<Long, JobRecord>();
		jobStats = new HashMap<String, JobStatistics>();
	}
	
	public synchronized static ServerMetrics getInstance() {
		if (sm == null) {
			sm = new ServerMetrics();
		}
		return sm;
	}
	
	public void put(JobMessage jobMessage) {
		try {
			if (!messageQueue.offer(jobMessage, 1, TimeUnit.SECONDS)) {
				logger.error("Can't add jobMessage to queue: " + jobMessage);
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public void startup() {
		startupTime = new Date();
		messageWorker.start();
		logger.info("ServerMetrics startups.");
	}
	
	public void shutdown() {
		messageWorker.shutdown();
		logger.info("ServerMetrics is shutdown successfully.");
	}
	
	public List<String> getHistoricalMessages() {
		List<String> messages = new ArrayList<String>();
		synchronized(historicalMessages) {
			messages.addAll(historicalMessages);
		}
		return Collections.unmodifiableList(messages);
	}
	
	public int getUnprocessedMessage() {
		return messageQueue.size();
	}
	
	
	public class JobMessageWorker extends Thread {		
		private boolean shouldRun = true;
		
		public JobMessageWorker() {
			this.setName("JobMessageWorker");
		}
		
		public void run() {
			while(shouldRun && !isInterrupted()) {
				try {
					JobMessage msg = messageQueue.take();
					synchronized(historicalMessages) {
						if (historicalMessages.size() >= ServerConstants.KEEP_MESSAGE_SIZE) {
							historicalMessages.remove(0);
						}
						historicalMessages.add(msg.formatMessage());
					}
					if (msg.getJobId() == -1) {
						continue;
					}
					long jobId = msg.getJobId();
					JobRecord jobRecord = null;
					if (runningJobs.containsKey(jobId)) {
						jobRecord = runningJobs.get(jobId);
					} else {
						if (msg.getState() != Job.State.SETUP) {
							logger.info("Invalid message: " + msg.formatMessage());
							continue;
						} else {
							jobRecord = new JobRecord(jobId, msg.getJobName());
							synchronized(runningJobs) {
								runningJobs.put(jobId, jobRecord);
							}
						}
					}
					
					if (jobRecord == null)	continue;
					
					synchronized(runningJobs) {
						jobRecord.addMessage(msg.getTime(), msg.getMsg());
						jobRecord.setState(msg.getState());
						if (msg.getState() == Job.State.SETUP) {
							jobRecord.setStartTime(new Date(msg.getTime()));
						}
						if (msg.getState() == Job.State.DONE) {
							jobRecord.setEndTime(new Date(msg.getTime()));
						}						
					}
					
					if (jobRecord.getState() == Job.State.DONE) {
						doJobDoneAction(jobId);
					}
					
				} catch (InterruptedException e) {
					logger.error(Thread.currentThread().getName() + " is interrupted at line: " + 
							e.getStackTrace()[e.getStackTrace().length-1].getLineNumber());
				}
			}
		}
		
		public void doJobDoneAction(long jobId) {
			if (!runningJobs.containsKey(jobId))	return;
			JobRecord jobRecord = runningJobs.get(jobId);
			logger.info(jobRecord.formatJobRecord());
			String jobName = jobRecord.getJobName();
			JobStatistics stats = null;
			if (!jobStats.containsKey(jobName)) {
				stats = new JobStatistics();
				stats.setJobName(jobName);
			} else {
				stats = jobStats.get(jobName);
			}
			stats.incCount(1);
			stats.incTotalTime(jobRecord.getEndTime().getTime() 
					- jobRecord.getStartTime().getTime());
			synchronized(runningJobs) {
				runningJobs.remove(jobId);
			}
			synchronized(jobStats) {
				jobStats.put(jobName, stats);
			}
		}
		
		public void shutdown() {
			this.interrupt();
			shouldRun = false;
			int size = messageQueue.size();
			if (size != 0) {
				logger.warn(String.format("There are %d messages unprocessed.", size));
			}
		}
	}
}
