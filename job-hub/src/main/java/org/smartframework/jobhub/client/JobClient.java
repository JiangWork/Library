package org.smartframework.jobhub.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.smartframework.jobhub.core.JobDefinition;
import org.smartframework.jobhub.core.JobException;
import org.smartframework.jobhub.protocol.ClientProtocol;
import org.smartframework.jobhub.utils.ParameterAccessor;

/**
 * This use to submit job to JobServer.
 *
 * @author jiangzhao
 * @date Jul 9, 2016
 * @version V1.0
 */
public class JobClient {
	private final Logger logger = Logger.getLogger(JobClient.class);
	private final static String DEFAULT_HOSTNAME = "localhost";
	private final static int DEFAULT_JOB_SERVER_PORT = 32100;
	private final static int DEFAULT_UPLOAD_SERVER_PORT = 32100;
	private final static int DEFAULT_UPDATE_INTERVAL = 1000;
	private final static boolean DEFAULT_SHOW_PROGRESS = true;
	
	private final static String HOSTNAME_KEY = "jobhub.hostname";
	private final static String JOBSERVER_PORT_KEY = "jobhub.jobserver.port";
	private final static String UPLOADSERVER_PORT_KEY = "jobhub.uploadserver.port";
	private final static String SHOW_PROGRESS_KEY = "jobhub.showprogress";
	private final static String UPDATE_INTERVAL_KEY = "jobhub.updateinterval";
		
	private final static String APP_CONFIG_FILE = "app.properties";
	
	private String hostName; // the job client server.
	private int jobServerPort;  // the port for submitting job.
	private int uploadServerPort; // the port for uploading files.
	
	private JobDefinition def;
	
	private List<String> jarsList;  // the depend jar files
	private List<String> resourcesList; // the resource files
	private Map<String, String> env;
	private Map<String, String> otherConfig;

	private ClientProtocol.Client client;
	
	private Properties prop;
	
	private long updateInterval;
	
	public JobClient() {
		jarsList = new ArrayList<String>();
		resourcesList = new ArrayList<String>();
		env = new HashMap<String, String>();
		otherConfig = new HashMap<String, String>();
		prop = new Properties();
		def = new JobDefinition();
		def.setJarsList(jarsList);
		def.setResourcesList(resourcesList);
		def.setEnv(env);
		def.setAllConfigMap(otherConfig);
		this.hostName = DEFAULT_HOSTNAME;
		this.jobServerPort = DEFAULT_JOB_SERVER_PORT;
		this.uploadServerPort = DEFAULT_UPLOAD_SERVER_PORT;
		this.updateInterval = DEFAULT_UPDATE_INTERVAL;
		InputStream is = JobClient.class.getClassLoader().getResourceAsStream(APP_CONFIG_FILE);
		if (is != null) {  // has configuration
			try {
				prop.load(is);
				ParameterAccessor pa = new ParameterAccessor(prop);
				this.hostName = pa.getString(HOSTNAME_KEY, DEFAULT_HOSTNAME);
				this.jobServerPort = pa.getInt(JOBSERVER_PORT_KEY, DEFAULT_JOB_SERVER_PORT);
				this.uploadServerPort = pa.getInt(UPLOADSERVER_PORT_KEY, DEFAULT_UPLOAD_SERVER_PORT);
				this.updateInterval = pa.getLong(UPDATE_INTERVAL_KEY, DEFAULT_UPDATE_INTERVAL);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		} else {
			System.out.println("No application config was found, default is used.");
		}
		System.out.println(String.format("\thostname:%s\n\tjobserver port:%d\n\t"
				+ "uploadserver port:%d\n\trefresh interval:%d\n", 
				this.hostName, this.jobServerPort, this.uploadServerPort,
			    this.updateInterval));
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
	
	public long getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getJobServerPort() {
		return jobServerPort;
	}

	public void setJobServerPort(int jobServerPort) {
		this.jobServerPort = jobServerPort;
	}

	/**
	 * Check the existence of files.
	 * @param def
	 * @throws JobException
	 */
	private void checkFiles(JobDefinition def) throws JobException {
		for(String jarName: def.getJarsList()) {
			File file = new File(jarName);
			if(!file.canRead()) {
				throw new JobException("Can't read jar file: " + jarName + ", wrong file name?");
			}
		}
		for (String resource: def.getResourcesList()) {
			File file = new File(resource);
			if(!file.canRead()) {
				throw new JobException("Can't read resource file: " + resource + ", wrong file name?");
			}
		}
	}
	
	private void uploadFiles(JobDefinition def) throws JobException {
		int total = def.getJarsList().size() + def.getResourcesList().size();
		System.out.println("Total upload files: " + total);
		int current = 1;
		UploadClient uploader = new UploadClient(this.hostName, this.uploadServerPort);
		List<String> allFiles = new ArrayList<String>();
		allFiles.addAll(def.getJarsList());
		allFiles.addAll(def.getResourcesList());
		for (String fileName: allFiles) {
			try {
				System.out.println(String.format("%d of %d: uploading %s", current++, total, fileName));
				boolean status = uploader.upload(fileName, def.getJobId());
				if (!status) {
					logger.error(uploader.getReply());
					throw new JobException(uploader.getReply());
				}
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
				throw new JobException(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Get new jobId, upload related files and submit the job.
	 * @throws JobException
	 */
	public void submitAsync() throws JobException {
		client = ClientProtocolClient.newClient(hostName, jobServerPort);
		if (client == null) {
			throw new JobException("Can't connect JobServer " + hostName + "@" + jobServerPort);
		}
		long jobId = -1;
		try {
			jobId = client.newJobId();
			def.setJobId(jobId);
			checkFiles(def);
			uploadFiles(def);
			ByteArrayOutputStream bos = new Byte
			ByteBuffer bb = ByteBuffer.wrap(array)
			client.submit(jobId, configData)
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobException(e.getMessage(), e);
		} finally {
			ClientProtocolClient.close(client);
		}
		System.out.println(String.format("Submit job to %s@%d successfully, returned jobid=%d.", hostName, jobServerPort, jobId));
		logger.info(String.format("Submit job to %s@%d successfully, returned jobid=%d.", hostName, jobServerPort, jobId));
	}
	
	
	
	public void submitSync() throws JobException {
		
	}
	
	
	
}
