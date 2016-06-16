package com.klatencor.klara.future.thrift.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftUtils {
	
	private static final Logger logger = Logger.getLogger(ThriftUtils.class);
	private static final Map<FutureService.Client, TTransport>	clientInfo 
				= new HashMap<FutureService.Client, TTransport>();	
	
	public static FutureService.Client newClient(String hostName, int port) {
		FutureService.Client client = null;
		logger.info("Obtaining client: " + client);
		for (int i = 0; i < 3; ++i) {
			try {
				TTransport transport = new TSocket(hostName, port);
				transport.open();
				TProtocol protocol = new TBinaryProtocol(transport);
				client = new FutureService.Client(protocol);
				clientInfo.put(client, transport);
				logger.info("Obtained client: " + client);
				break;
			} catch (TTransportException e) {
				logger.error(
						"Obtain client at try " + i + ": " + e.getMessage(), e);
			}
		}
		return client;
	}
	
	public static void closeClient(FutureService.Client client) {
		if (client != null) {
			if (clientInfo.containsKey(client)) {
				logger.info("Closing client: " + client);
				TTransport transport = clientInfo.get(client);
				transport.close();
				clientInfo.remove(client);
			}
		}
	}
	
	public static TServer newServer(int port, FutureService.Iface impl) throws TTransportException {
		 TServerTransport serverTransport = new TServerSocket(port);
		 TProcessor processor = new FutureService.Processor<FutureService.Iface>(impl);
		 TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
		 
		 return server;
	}
}
