package com.klatencor.klara.future.server.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.job.JobState;
import com.klatencor.klara.future.server.ServerConfiguration;

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
	
	private AtomicLong jobIdCounter;
	
	private boolean isRunning = false;
	
	public ServerMetrics() {
		startupTime = new Date(System.currentTimeMillis());
		messageQueue = new LinkedBlockingQueue<JobMessage>();
		messageWorker = new JobMessageWorker();
		historicalMessages = new ArrayList<String>();
		runningJobs = new HashMap<Long, JobRecord>();
		jobStats = new HashMap<String, JobStatistics>();
		jobIdCounter = new AtomicLong(1);
	}
	
	public void put(JobMessage jobMessage) {
		if (!isRunning) {
			return;
		}
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
		isRunning = true;
		logger.info("ServerMetrics startups at " + startupTime);
	}
	
	public void shutdown() {
		messageWorker.shutdown();
		isRunning = false;
		logger.info("ServerMetrics is shutdown successfully.");
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public Date getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(Date startupTime) {
		this.startupTime = startupTime;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public List<String> getHistoricalMessages() {
		List<String> messages = new ArrayList<String>();
		synchronized(historicalMessages) {
			messages.addAll(historicalMessages);
		}
		return messages;
	}
	
	public long getJobIdAndInc() {
		return jobIdCounter.getAndIncrement();
	}
	
	public int getUnprocessedMessage() {
		return messageQueue.size();
	}
	
	public boolean hasUncompletedJob() {
		return getUncompletedJobNumber() != 0;
	}
	
	public int getUncompletedJobNumber() {
		int runningJobCount = 0;
		synchronized(runningJobs) {
			runningJobCount = runningJobs.size();
		}
		return runningJobCount;
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
					logger.debug(String.format("Processing message: %s", msg.formatMessage()));
					synchronized(historicalMessages) {
						if (historicalMessages.size() >= ServerConfiguration.KEEP_MESSAGE_SIZE) {
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
						if (msg.getState() != JobState.SETUP) {
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
						if (msg.getState() == JobState.SETUP) {
							jobRecord.setStartTime(new Date(msg.getTime()));
						}
						if (msg.getState() == JobState.DONE) {
							jobRecord.setEndTime(new Date(msg.getTime()));
						}						
					}
					
					if (jobRecord.getState() == JobState.DONE) {
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
			synchronized(jobStats) {
				if (!jobStats.containsKey(jobName)) {
					stats = new JobStatistics();
					stats.setJobName(jobName);
				} else {
					stats = jobStats.get(jobName);
				}
				stats.incCount(1);
				stats.incTotalTime(jobRecord.getEndTime().getTime() 
						- jobRecord.getStartTime().getTime());
				jobStats.put(jobName, stats);
			}
			logger.info("Job statistics: " + stats.formartJobStatistics());
			synchronized(runningJobs) {
				runningJobs.remove(jobId);
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
