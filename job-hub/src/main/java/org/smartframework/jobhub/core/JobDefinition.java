package org.smartframework.jobhub.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.smartframework.jobhub.utils.IOUtils;
import org.smartframework.jobhub.utils.StringUtils;


public class JobDefinition {
	
	private final static Logger logger = Logger.getLogger(JobDefinition.class);
	
	public final static String JOB_NAME_KEY = "job.name";
	public final static String JOB_MAINCLASS_KEY = "job.mainclass";
	public final static String JOB_METHOD_KEY = "job.method.name";
	public final static String JOB_METHODARGS_KEY = "job.method.args";
	public final static String JOB_SUBMITTER_KEY = "job.submitter";
	public final static String JOB_TIMEOUT_KEY = "job.timeout";
	public final static String JOB_JARS_KEY = "job.jars";
	public final static String JOB_RESOURCES_KEY = "job.resources";
	public final static String JOB_ENV_KEY = "job.env";
	
	private String jobName;
	private String mainClass;
	private String enterMethod;
	private String enterArgs;
	private String submitter;
	private long timeout;
	private List<String> jarsList;  // the depend jar files
	private List<String> resourcesList; // the resource files
	private Map<String, String> env;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getMainClass() {
		return mainClass;
	}
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}
	public String getEnterMethod() {
		return enterMethod;
	}
	public void setEnterMethod(String enterMethod) {
		this.enterMethod = enterMethod;
	}
	public String getSubmitter() {
		return submitter;
	}
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public List<String> getJarsList() {
		return jarsList;
	}
	public void setJarsList(List<String> jarsList) {
		this.jarsList = jarsList;
	}
	public List<String> getResourcesList() {
		return resourcesList;
	}
	public void setResourcesList(List<String> resourcesList) {
		this.resourcesList = resourcesList;
	}
	public Map<String, String> getEnv() {
		return env;
	}
	public void setEnv(Map<String, String> env) {
		this.env = env;
	}
	
	public void read(String path) {
		
	}
	
	public boolean write(String path) throws IOException {
		boolean status = false;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(JOB_NAME_KEY + "=" + StringUtils.stringfyObject(jobName) + "\n");
			bw.write(JOB_MAINCLASS_KEY + "=" + StringUtils.stringfyObject(mainClass) + "\n");
			bw.write(JOB_METHOD_KEY + "=" + StringUtils.stringfyObject(enterMethod) + "\n");
			bw.write(JOB_METHODARGS_KEY + "=" + StringUtils.stringfyObject(enterArgs) + "\n");
			bw.write(JOB_SUBMITTER_KEY + "=" + StringUtils.stringfyObject(submitter) + "\n");
			bw.write(JOB_TIMEOUT_KEY + "=" + StringUtils.stringfyObject(timeout) + "\n");
			bw.write(JOB_JARS_KEY + "=" + StringUtils.stringfyList(jarsList) + "\n");
			bw.write(JOB_RESOURCES_KEY + "=" + StringUtils.stringfyList(resourcesList) + "\n");
			bw.write(JOB_ENV_KEY + "=" + StringUtils.stringfyMap(env) + "\n");
			bw.close();
			status = true;
		} catch(IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			IOUtils.closeQuietly(bw);
		}
		return status;
	}
}
