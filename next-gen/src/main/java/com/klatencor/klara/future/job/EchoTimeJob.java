package com.klatencor.klara.future.job;

import java.util.HashMap;
import java.util.Map;


/**
 * This is a simple job to demonstrate the work flow.
 * This job will return the current server time.
 * 
 * @author jiangzhao
 * @date Jun 16, 2016
 * @version V1.0
 */
public class EchoTimeJob extends DefaultJob {

	public EchoTimeJob(long jobId) {
		super(jobId, "echoTime", null);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void execute() throws Exception {
		JobResult result = new JobResult();
		result.setStatus(true);
		result.setReason("");
		Map<String, String> output = new HashMap<String, String>();
		output.put("server.currenttime", String.valueOf(System.currentTimeMillis()));
		result.setOutput(output);
		this.setJobResult(result);
	}


}
