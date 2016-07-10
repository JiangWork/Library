package org.smartframework.jobhub.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.smartframework.jobhub.core.JobDefinition;
import org.smartframework.jobhub.core.JobException;
import org.smartframework.jobhub.protocol.ClientProtocol;
import org.smartframework.jobhub.protocol.impl.ClientProtocolClient;

/**
 * This use to submit job to JobServer.
 *
 * @author jiangzhao
 * @date Jul 9, 2016
 * @version V1.0
 */
public class JobClient {
	private final Logger logger = Logger.getLogger(JobClient.class);
	
	private String hostName; // the job client server
	private int port;  // the port
	private JobDefinition def;
	
	private List<String> jarsList;  // the depend jar files
	private List<String> resourcesList; // the resource files
	private Map<String, String> env;
	private Map<String, String> otherConfig;

	private ClientProtocol.Client client;
	
	private boolean showProgress;
	private long updateInterval;
	
	public JobClient(String hostName, int port) {
		jarsList = new ArrayList<String>();
		resourcesList = new ArrayList<String>();
		env = new HashMap<String, String>();
		otherConfig = new HashMap<String, String>();
		def = new JobDefinition();
		this.hostName = hostName;
		this.port = port;
		def.setJarsList(jarsList);
		def.setResourcesList(resourcesList);
		def.setEnv(env);
		def.setAllConfigMap(otherConfig);
		showProgress = true;
		updateInterval = 1 * 1000; //  1ms
	}
	
	public void addJar(String jarName) {
		this.jarsList.add(jarName);
	}
	
	public void addResource(String resourceName) {
		this.resourcesList.add(resourceName);
	}
	
	public void addEnv(String key, String value) {
		this.env.put(key, value);
	}
	
	public void addProperty(String key, String value) {
		this.otherConfig.put(key, value);
	}
	
	public void setJobName(String jobName) {
		def.setJobName(jobName);
	}

	public void setMainClass(String mainClass) {
		def.setMainClass(mainClass);
	}

	public void setEnterMethod(String enterMethod) {
		def.setEnterMethod(enterMethod);
	}

	public void setEnterArgs(String enterArgs) {
		def.setEnterArgs(enterArgs);
	}

	public void setSubmitter(String submitter) {
		def.setSubmitter(submitter);
	}

	public void setTimeout(long timeout) {
		def.setTimeout(timeout);
	}
	
	public synchronized boolean isShowProgress() {
		return showProgress;
	}

	public synchronized void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public synchronized long getUpdateInterval() {
		return updateInterval;
	}

	public synchronized void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	public void submitAsync() throws JobException {
		client = ClientProtocolClient.newClient(hostName, port);
		if (client == null) {
			throw new JobException("Can't connect JobServer " + hostName + "@" + port);
		}
		try {
			long jobId = client.newJobId();
		} catch (TException e) {
			logger.error(e.getMessage(), e);
		}
		
		ClientProtocolClient.close(client);
	}
	
	public void submitSync() throws JobException {
		
	}
	
	
	
}
