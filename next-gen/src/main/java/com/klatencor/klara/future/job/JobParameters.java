package com.klatencor.klara.future.job;

import java.util.Map;

import com.klatencor.klara.future.thrift.common.ParameterAccessor;

public class JobParameters extends ParameterAccessor {

	private Map<String, String> parameters;
	
	public JobParameters(Map<String, String> parameters) {
		super(parameters);
		this.parameters = parameters;
	}
	
	public Map<String, String> getRawParameters() {
		return parameters;
	}

}
