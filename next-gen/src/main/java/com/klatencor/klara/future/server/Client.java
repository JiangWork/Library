package com.klatencor.klara.future.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;

import com.klatencor.klara.future.thrift.common.FutureService;
import com.klatencor.klara.future.thrift.common.Request;
import com.klatencor.klara.future.thrift.common.Response;
import com.klatencor.klara.future.thrift.common.ThriftUtils;

/**
 * A future client application to communicate with sever
 * and send some requests to sever.
 * 
 * @author jiangzhao
 * @date  Jun 20, 2016
 * @version V1.0
 */
public class Client {
	
	public static void printUsage() {
		String usage = "Utilities to manage future server: util options [global-config]\n"
				+ "Supported options:\n"
				+ "\tstop [force]: stop the server by force if optional [force] is set as 1, default is 0.\n"
				+ "\tserverinfo [mem|runningJob|completedJob|msg]: get the server running information since it startups."
				+ "\tsubmit jobType [options]: submit a job. As follows:\n"
				+ "\t\tstoreRecipe recipePath\n"
				+ "\t\tgenerateXML recipePath xmlFilename (without suffix)\n"
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

		FutureService.Iface client = ThriftUtils.newClient(hostName, port);
		if (client == null) {
			System.err.println("No future server is running at " + hostName + "@" + port);
			System.exit(-1);
		}
		
		if (args[0].equals("stop")) {
			String force = getArgs(args, 1, "0");
			client.shutdown("1".equals(force));
			System.out.println("Sever " + hostName + "@" + port + " is stopped successfully.");
		} else if (args[0].equals("msg")) {
			
		} else if (args[0].equals("job")) {
			
		} else if (args[0].equals("mem")) {
			
		} else if (args[0].equals("submit")) {
			String jobType = getArgs(args, 1, "");
			if (jobType.equals("storeRecipe")) {
				long start = System.currentTimeMillis();
				Request request = new Request();
				request.setWho("futureClient");
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("recipePath", getArgs(args, 2, ""));
				parameters.put("newOrUpdate", "new");
				parameters.put("fmid", "0");
				request.setParameters(parameters);
				Response response = client.storeRecipe(
						System.currentTimeMillis(), request);
				if (!response.isStatus()) {
					System.err.println("Job fails:" + response.getFailure());
				} else {
					System.out.println("Store recipe successfully, FmId:"
							+ response.ret.get("fmid") + ", elapsed time:"
							+ (System.currentTimeMillis() - start) + "ms");
				}
			} else if (jobType.equals("generateXML")) {
				long start = System.currentTimeMillis();
				Response response = client.generateXML(
						System.currentTimeMillis(), getArgs(args, 2, ""),
						getArgs(args, 3, ""), "futureClient");
				if (!response.isStatus()) {
					System.err.println("Job fails:" + response.getFailure());
				} else {
					System.out.println("Gnerate XML file successfully"
							+ ", elapsed time:"
							+ (System.currentTimeMillis() - start) + "ms, file path:"
							+ getArgs(args, 3, "") + ".xml");
				}
			} else {
				printUsage();
			}
		} else {
		
			printUsage();
		}
		
		
		ThriftUtils.closeClient(client);
	}
	
	public static String getArgs(String[] args, int index, String defaultArg) {
		if (index < 0 || index >= args.length) {
			return defaultArg;
		}
		return args[index];
	}
}
