package org.smartframework.shell.process;

import org.smartframework.shell.process.InMemoryOutputStream;

public class InMemoryOutputStreamTest {

	public static void main(String[] args) {
		InMemoryOutputStream ios = new InMemoryOutputStream(true, 32, 2);
		ios.write("hi".getBytes());
		System.out.println(ios.toString());
		ios.write(" zhaojiang".getBytes());
		System.out.println(ios.toString());
		ios.write("This is example for inMemoryOutputStream".getBytes());
		System.out.println(ios.toString());
		ios.write(" hi".getBytes());
		System.out.println(ios.toString());
		ios.write(" overflow".getBytes());
		System.out.println(ios.toString());
		ios.write("This page is your source to download or update your existing Java Runtime Environment (JRE, Java Runtime), ".getBytes());
		System.out.println(ios.toString());
		ios.close();
	}

}
