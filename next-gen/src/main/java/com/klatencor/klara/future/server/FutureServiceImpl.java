package com.klatencor.klara.future.server;

import org.apache.thrift.TException;

import com.klatencor.klara.future.thrift.common.FutureService;
import com.klatencor.klara.future.thrift.common.Reponse;
import com.klatencor.klara.future.thrift.common.Request;

public class FutureServiceImpl implements  FutureService.Iface {

	@Override
	public Reponse storeRecipe(long time, Request request) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
