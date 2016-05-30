package org.smartframework.system.job;

public interface JobConf {
	
	public void setAttribute(String key, Object val);
	
	public Object getAttribute(String key, Object defaultVal);
	
}
