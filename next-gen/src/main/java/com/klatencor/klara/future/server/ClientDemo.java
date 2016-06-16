package com.klatencor.klara.future.server;

import java.util.Date;

import org.apache.thrift.TException;

import com.klatencor.klara.future.thrift.common.FutureService;
import com.klatencor.klara.future.thrift.common.Response;
import com.klatencor.klara.future.thrift.common.ThriftUtils;

public class ClientDemo {

	public static void main(String[] args) throws TException, InterruptedException {
		// TODO Auto-generated method stub
		FutureService.Iface client = ThriftUtils.newClient("localhost", 32100);
		Response response = client.whatTime();
		if (response.status) {
			System.out.println(new Date(Long.parseLong(response.ret.get("server.currenttime"))));
		}
		
		FutureService.Iface client2 = ThriftUtils.newClient("localhost", 32100);
		response = client2.whatTime();
		if (response.status) {
			System.out.println(new Date(Long.parseLong(response.ret.get("server.currenttime"))));
		}
		client2.shutdown(true);
		Thread.sleep(1000);
		FutureService.Iface client3 = ThriftUtils.newClient("localhost", 32100);
		if (client3 == null) {
			System.out.println("Shutdown successfully.");
		}
		ThriftUtils.closeClient(client2);
		
		ThriftUtils.closeClient(client);
	}

}
