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

public class TUtils {
	
	private static final Logger logger = Logger.getLogger(TUtils.class);
	private static final Map<Future.Client, TTransport>	clientInfo 
				= new HashMap<Future.Client, TTransport>();	
	
	public static Future.Client newClient(String hostName, int port) {
		Future.Client client = null;
		logger.info("Obtaining client: " + client);
		for (int i = 0; i < 3; ++i) {
			try {
				TTransport transport = new TSocket(hostName, port);
				transport.open();
				TProtocol protocol = new TBinaryProtocol(transport);
				client = new Future.Client(protocol);
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
	
	public static void closeClient(Future.Client client) {
		if (client != null) {
			if (clientInfo.containsKey(client)) {
				logger.info("Closing client: " + client);
				TTransport transport = clientInfo.get(client);
				transport.close();
				clientInfo.remove(client);
			}
		}
	}
	
	public static TServer newServer(int port, Future.Iface impl) throws TTransportException {
		 TServerTransport serverTransport = new TServerSocket(port);
		 TProcessor processor = new Future.Processor<Future.Iface>(impl);
		 TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
		 
		 return server;
	}
}
