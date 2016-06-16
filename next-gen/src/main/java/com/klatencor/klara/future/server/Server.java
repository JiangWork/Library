package com.klatencor.klara.future.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.klatencor.klara.future.thrift.common.ThriftUtils;

public class Server {
	
	private static Logger logger = Logger.getLogger(Server.class);
	
	private static ServerContext ctx;
	private static TServer server;
	private static ConfigurableApplicationContext beanFactory;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public static void startServer(int port) throws TTransportException {
		FutureServiceImpl impl = new FutureServiceImpl();
	    server = ThriftUtils.newServer(port, impl);
	    server.serve();
	    System.out.println(new Date() + ": server starts at port " + port);
	    logger.info("Server starts at port: " + port);	    
		beanFactory = new ClassPathXmlApplicationContext("spring-beans.xml");		
		ctx = beanFactory.getBean("serverContext", ServerContext.class);
		ctx.getMetrics().startup();  // start ServerMetrics
	}
	
	public static void stopServer(boolean force) {
		if (server != null && server.isServing()) {
			if (!ctx.getMetrics().hasUncompletedJob() || force) {
				ctx.getMetrics().shutdown();
				server.stop();
				beanFactory.close();
				logger.info("stop server ");
			} else {
				logger.info("server isn't stopped due to running jobs.");
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
