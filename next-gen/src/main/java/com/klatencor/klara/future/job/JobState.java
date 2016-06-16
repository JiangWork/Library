package com.klatencor.klara.future.job;

/**
 * Define job state.
 * 
 * @author jiangzhao
 * @date  Jun 14, 2016
 * @version V1.0
 */
public enum JobState {
	CREATED("created"),
	SETUP("setup"),
	RUNNING("running"),
	CLEAN("clean"),
	DONE("done");
	
	String val;
	
	JobState(String val) {
		this.val = val;
	}

}
