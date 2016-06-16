package com.klatencor.klara.future.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import com.klatencor.klara.future.job.DefaultJob;
import com.klatencor.klara.future.job.EchoTimeJob;
import com.klatencor.klara.future.job.JobParameters;
import com.klatencor.klara.future.job.JobRunner;
import com.klatencor.klara.future.job.StoreRecipeJob;
import com.klatencor.klara.future.thrift.common.FutureService;
import com.klatencor.klara.future.thrift.common.Response;
import com.klatencor.klara.future.thrift.common.Request;


/**
 * A implementation of FutureService interface.
 *
 * @author jiangzhao
 * @date Jun 16, 2016
 * @version V1.0
 */
public class FutureServiceImpl implements FutureService.Iface {
	
	private final static Logger logger = Logger.getLogger(FutureServiceImpl.class);

	private Server server;
	
	public FutureServiceImpl(Server server) {
		this.server = server;
	}
	
	@Override
	public Response storeRecipe(long time, Request request) throws TException {
		logger.info(String.format("Reciving storeRecipe task from %s, created at %s", 
				request.who, new Date(time).toString()));
		StoreRecipeJob job = new StoreRecipeJob(0);
		return runJob(job, request);
	}


	@Override
	public Response whatTime() throws TException {
		long jobId = Server.getContext().getMetrics().getJobIdAndInc();
		EchoTimeJob job = new EchoTimeJob(jobId);
		JobRunner runner = new JobRunner(job, Server.getContext().getReporter());
		logger.info("about to run job " + job.getJobId());
		runner.run();
		return runner.getResponse();
	}

	@Override
	public void shutdown(boolean force) throws TException {
		this.server.stopServer(force);
	}
	
	public Response runJob(DefaultJob job, Request request) {
		long jobId = Server.getContext().getMetrics().getJobIdAndInc();
		job.setJobId(jobId);
		job.setParameters(new JobParameters(request.parameters));
		JobRunner runner = new JobRunner(job, Server.getContext().getReporter());
		logger.info("about to run job " + job.getJobId());
		runner.run();
		return runner.getResponse();
	}
	
	

}
