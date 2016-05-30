package org.smartframework.mvc.dispatcher.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.smartframework.common.utils.StringUtils;
import org.smartframework.mvc.dispatcher.annotation.Init;
import org.smartframework.mvc.dispatcher.core.ParameterAware;
import org.smartframework.mvc.dispatcher.support.WordCountSubmitter;


/**
 * 
 * A simple controller to submit/query jobs to Hadoop. 
 *
 * @author Miller
 * @date Mar 28, 2016
 */
public class HadoopJobController implements ParameterAware {

	private static final Logger logger = Logger.getLogger(HadoopJobController.class);
	private Map<String, String[]> paramMap;
	private WordCountSubmitter wordCountClient;
	
	public void setParameter(Map<String, String[]> parameter) {
		this.paramMap = parameter;
	}
	
	@Init
	public void init() {
		wordCountClient = new WordCountSubmitter();
	}
	
	/**
	 * Submit the job and wait the completion of the Job
	 * @return the output of Map-Reduce
	 */
	public String submitAndWait() {
		String[] inputs = paramMap.get("hadoop_input");
		if (inputs == null) {
			return "Hadoop input directory is needed,";
		}
		String[] outputs = paramMap.get("hadoop_output");
		if (outputs == null) {
			return "Hadoop output directory is needed,";
		}
		String input = inputs[0];
		String output = outputs[0];
		logger.info(String.format("submitAndWait is called, input:%s, output: %s", input, output));
		if (StringUtils.isEmpty(input) || StringUtils.isEmpty(output)) {
			return "Input or output can't be empty.";
		}
		try {
			return wordCountClient.submitAndWait(input, output);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			return StringUtils.stringifyException(e);
		}
	}

	/**
	 * Submit the job and return the Job information immediately.
	 * @return the jobId
	 */
	public String submit() {
		String[] inputs = paramMap.get("hadoop_input");
		if (inputs == null) {
			return "Hadoop input directory is needed.";
		}
		String[] outputs = paramMap.get("hadoop_output");
		if (outputs == null) {
			return "Hadoop output directory is needed.";
		}
		String input = inputs[0];
		String output = outputs[0];
		logger.info(String.format("submit is called, input:%s, output: %s", input, output));
		if (StringUtils.isEmpty(input) || StringUtils.isEmpty(output)) {
			return "Input or output can't be empty.";
		}
		try {
			return wordCountClient.submit(input, output);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			return StringUtils.stringifyException(e);
		}
	}
	
	public String status() {
		logger.info("status() is called.");
		String[] jobIds = paramMap.get("jobId");
		if (jobIds == null) {
			return "jobId is needed, please set at URL.";
		}
		try {
			String jobId = jobIds[0];
			return wordCountClient.query(jobId);
		} catch (IOException e) {
			return StringUtils.stringifyException(e);
		}
	}
	
//	public String kill() {
//		String jobId = paramMap.get("jobID")[0];
//		
//	}
	
}
