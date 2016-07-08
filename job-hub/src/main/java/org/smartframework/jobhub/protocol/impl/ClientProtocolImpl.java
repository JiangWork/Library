package org.smartframework.jobhub.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.smartframework.jobhub.core.JobDefinition;
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

	public class ClientProtocolImplDelegate  {

		private final Logger logger = Logger.getLogger(ClientProtocolImplDelegate.class);
		
		
		public long newJobId() {
			return jobManager.getJobId();
		}


		public void submit(long jobId, ByteBuffer configData) throws Exception {
			JobDefinition def = new JobDefinition();
			byte[] buf = configData.array();
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf));
			def.read(dis);
				
			
		}


		public ActionResult kill(long jobId) throws TException {
			// TODO Auto-generated method stub
			return null;
		}

		public JobStatus query(long jobId) throws TException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
