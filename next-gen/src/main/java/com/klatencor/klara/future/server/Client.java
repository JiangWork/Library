package com.klatencor.klara.future.server;

import java.util.Date;

import org.apache.thrift.TException;

import com.klatencor.klara.future.thrift.common.FutureService;
import com.klatencor.klara.future.thrift.common.Response;
import com.klatencor.klara.future.thrift.common.ThriftUtils;

public class Client {
	
	public static void printUsage() {
		String usage = "Utilities to manage future server: util options [global-config]\n"
				+ "Supported options:\n"
				+ "\tstop [force]: stop the server by force if optional [force] is set as 1, default is 0."
				+ "\tmsg [number]: print the latest [number] messages handled by sever if possible, default [number] is 10."
				+ "\tjob -r|-c:    print running or completed job information since the server startups."
				+ "\tmem: print the memory usage.\n"
				+ "Optional global-config:\n"
				+ "\t-host hostname: specify the host running future server, default is localhost.\n"
				+ "\t-port portnumber: specify the port number, default is 32100.\n";
		System.err.println(usage);
	}

	public static void main(String[] args) throws TException, InterruptedException {
		if (args.length == 0) {
			printUsage();
			System.exit(-1);
		}
		
		String hostName = "localhost";
		int port = 32100;		
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-host")) {
				if (i == args.length - 1) {
					System.err.println("No hostname is specified.");
					printUsage();
					System.exit(-1);
				} else {
					hostName = args[++i];
				}
			}
			if (args[i].equals("-port")) {
				if (i == args.length - 1) {
					System.err.println("No port number is specified.");
					printUsage();
					System.exit(-1);
				} else {
					try {
						port = Integer.parseInt(args[++i]);
					} catch(Exception e) {
						// ignored
					}
				}
			}
		}
		// TODO Auto-generated method stub
		FutureService.Iface client = ThriftUtils.newClient(hostName, port);
		if (client == null) {
			System.err.println("No future server is running at " + hostName + "@" + port);
			System.exit(-1);
		}
		
		if (args[0].equals("stop")) {
			String force = getArgs(args, 1, "0");
			client.shutdown("1".equals(force));
		} else if (args[0].equals("msg")) {
			
		} else if (args[0].equals("job")) {
			
		} else if (args[0].equals("mem")) {
			
		} else {
			printUsage();
		}
		
		
		ThriftUtils.closeClient(client);
		
//		Response response = client.whatTime();
//		if (response.status) {
//			System.out.println(new Date(Long.parseLong(response.ret.get("server.currenttime"))));
//		}
//		
//		FutureService.Iface client2 = ThriftUtils.newClient("localhost", 32100);
//		response = client2.whatTime();
//		if (response.status) {
//			System.out.println(new Date(Long.parseLong(response.ret.get("server.currenttime"))));
//		}
//		client2.shutdown(true);
//		Thread.sleep(1000);
//		FutureService.Iface client3 = ThriftUtils.newClient("localhost", 32100);
//		if (client3 == null) {
//			System.out.println("Shutdown successfully.");
//		}
//		ThriftUtils.closeClient(client2);
//		
//		ThriftUtils.closeClient(client);
	}
	
	public static String getArgs(String[] args, int index, String defaultArg) {
		if (index < 0 || index >= args.length) {
			return defaultArg;
		}
		return args[index];
	}
}
