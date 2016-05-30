package org.smartframework.mvc.dispatcher.core;

import java.io.IOException;

import org.apache.hadoop.util.Shell;

public class ShellTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(Shell.execCommand("/bin/ls -lrt"));
	}

}
