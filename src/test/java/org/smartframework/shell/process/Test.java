package org.smartframework.shell.process;

import library.shell.Proc;
import library.shell.ProcResult;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ProcResult result = new Proc.ProcBuilder("kill","kill").
				withArgs("-9", "10750").
				withTimeOut(1000).
				build().
				run();
		System.out.println(result);
	}

}
