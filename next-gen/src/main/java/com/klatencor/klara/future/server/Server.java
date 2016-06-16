package com.klatencor.klara.future.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.klatencor.klara.future.thrift.common.ThriftUtils;

public class Server {
	
	private static Logger logger = Logger.getLogger(Server.class);
	
	private static ServerContext ctx;
	private static TServer tserver;
	private static ConfigurableApplicationContext beanFactory;
	
	private Thread startThriftThread;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		try {
			server.startServer(32100);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.out.println("Start server fails due to: " + e.getMessage());
			System.exit(-1);
		}
	}
	
	
	public void startServer(int port) throws Exception {
		FutureServiceImpl impl = new FutureServiceImpl(this);
		tserver = ThriftUtils.newServer(port, impl);
		startThriftServer(tserver);
		long timeout = 10 * 1000 + System.currentTimeMillis();
		while(!tserver.isServing()) {
			Thread.sleep(1000);
			if (System.currentTimeMillis() > timeout) {
				throw new Exception(String.format("Server can't startup after waiting %s s.", 10));
			}
		}
	    System.out.println(new Date() + ": server starts at port " + port);
	    logger.info("Server starts at port " + port);	    
		beanFactory = new ClassPathXmlApplicationContext("spring-beans.xml");		
		ctx = beanFactory.getBean("serverContext", ServerContext.class);
		ctx.getMetrics().startup();  // start ServerMetrics
	}
	
	public void startThriftServer(TServer tserver) {
		startThriftThread = new Thread() {
			public void run() {
				this.setName("ThriftSeverThread");
				tserver.serve();
			}
		};
		startThriftThread.start();
	}
	
	public void stopServer(boolean force) {
		if (tserver != null && tserver.isServing()) {
			if (!ctx.getMetrics().hasUncompletedJob() || force) {
				ctx.getMetrics().shutdown();
				tserver.stop();
				beanFactory.close();
				logger.info("stop the server successfully.");
				System.exit(0);
			} else {
				logger.info("server isn't stopped due to running jobs, #=" + ctx.getMetrics().getUncompletedJobNumber());
			}
		}
	}
	
	
	public static ServerContext getContext() {
		if (ctx == null)  {
			throw new IllegalStateException("ServerContext hasn't been instantiated.");
		}
		return ctx;
			
	}

}
