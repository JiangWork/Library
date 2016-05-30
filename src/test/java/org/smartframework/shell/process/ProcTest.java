package org.smartframework.shell.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.smartframework.shell.process.Proc;
import org.smartframework.shell.process.ProcResult;

public class ProcTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		
		ProcResult result = new Proc.ProcBuilder("echoCaller","echo", "hello world").build().run();
		System.out.println(result);
		
		new Proc.ProcBuilder("echoCaller","echo", "hello world").withOutputStream(output).build().run();
		System.out.println(output.toString());
		
		result = new Proc.ProcBuilder("bashCaller","bash", "-c",  "echo $VAR $VAR2")
		.withVar("VAR", "ZHAO").withVar("VAR2", "JIANG").build().run();
		System.out.println(result);
		
		ByteArrayInputStream input = new ByteArrayInputStream("Hello cruel World".getBytes());

		result = new Proc.ProcBuilder("wcCaller","wc")
		        .withArg("-w")
		        .withInputStream(input).build().run();
		System.out.println(result);
		
		
		result = new Proc.ProcBuilder("catCaller","cat")
        .withInput("Hello world!").build().run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("bashCaller","bash")
		.withArg("-c").withArgs("ls . | wc -l")
		.build().run();
		System.out.println(result);
		
		
//		result = new Proc.ProcBuilder("pwdCaller","pwd")
//		.workingDirectory("/kla/klaS/sxlink")
//        .run();
//		System.out.println(result);
//		
//		result = new Proc.ProcBuilder("nosuchcmdCaller","pd")
//		.workingDirectory("/kla/klaS/sxlink")
//        .run();
//		System.out.println(result);
		
		result = new Proc.ProcBuilder("catCaller","cat")
        .withArg("pom.xml").build().run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("sleepCaller","sleep")
        .withArg("2").withTimeOut(1000).build().run();
		System.out.println(result);
		
		
	}

}
