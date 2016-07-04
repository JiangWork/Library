package org.smartframework.jobhub.protocol.impl;

import java.nio.ByteBuffer;

import org.apache.thrift.TException;
import org.smartframework.jobhub.core.JobManager;
import org.smartframework.jobhub.protocol.ActionResult;
import org.smartframework.jobhub.protocol.ClientProtocol;
import org.smartframework.jobhub.protocol.JobStatus;
import org.smartframework.jobhub.protocol.ClientProtocol.Iface;

/**
 * 
 * @author Miller
 * @date Jul 2, 2016 10:03:52 PM
 */
public class ClientProtocolImpl implements ClientProtocol.Iface {
	
	private JobManager jobManager;
	
	public ClientProtocolImpl(JobManager jobManager) {
		this.jobManager = jobManager;
	}
	
	@Override
	public long newJobId() throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ActionResult submit(long jobId, ByteBuffer configData) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionResult kill(long jobId) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobStatus query(long jobId) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
