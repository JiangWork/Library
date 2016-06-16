package com.klatencor.klara.future.job;

import java.util.Map;

import com.klatencor.klara.future.thrift.common.Response;

/**
 * This class contains the result of a job. 
 *
 * @author jiangzhao
 * @date Jun 16, 2016
 * @version V1.0
 */
public class JobResult {
	
    /**A flag indicates the job is executed success or not.**/
	private boolean status;
	
	/**the reason of failure.**/
	private String reason;
	
	/**the output information of the job.**/
	private Map<String, String> output;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, String> getOutput() {
		return output;
	}

	public void setOutput(Map<String, String> output) {
		this.output = output;
	}
	
	public Response createResponse() {
		Response response = new Response();
		response.setStatus(status);
		response.setFailure(reason);
		response.setRet(output);
		return response;
	}
	
}
