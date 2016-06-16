package com.klatencor.klara.future.job;

/**
 * A job with functionality of reporting current job status
 * to administrator.
 * 
 * 
 * @author jiangzhao
 * @date  Jun 14, 2016
 * @version V1.0
 */
public interface ReportableJob extends JobDefinition {

	public void report(String message);
}
