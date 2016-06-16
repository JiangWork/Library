package com.klatencor.klara.future.job;

import java.util.Map;

import com.klatencor.klara.future.thrift.common.ParameterAccessor;

/**
 * This class provide necessary information to the job via
 * {@link HashMap}, also provide a set of access methods to get
 * the parameters in different types.
 *
 * @author jiangzhao
 * @date Jun 16, 2016
 * @version V1.0
 */

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
