package org.smartframework.jobhub.protocol.impl;

import org.apache.thrift.TException;
import org.smartframework.jobhub.core.JobManager;
import org.smartframework.jobhub.protocol.InnerProtocol;
import org.smartframework.jobhub.protocol.InnerProtocol.Iface;

/**
 * A InnerProtocolImpl used to report job progress.
 * 
 * @author Miller
 * @date Jul 2, 2016 10:32:11 PM
 */
public class InnerProtocolImpl implements InnerProtocol.Iface {

	private JobManager jobManager;
	
	public InnerProtocolImpl(JobManager jobManager) {
		this.jobManager = jobManager;
	}
	
	@Override
	public boolean progress(double precent) throws TException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean message(String message) throws TException {
		// TODO Auto-generated method stub
		return false;
	}

}
