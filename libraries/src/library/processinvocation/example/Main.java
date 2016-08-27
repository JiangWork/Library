package library.processinvocation.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import library.processinvocation.Proc;
import library.processinvocation.ProcResult;
import library.processinvocation.Proc.ProcBuilder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		
		ProcResult result = new Proc.ProcBuilder("echoCaller","echo", "hello world").run();
		System.out.println(result);
		
		new Proc.ProcBuilder("echoCaller","echo", "hello world").withOutputStream(output).run();
		System.out.println(output.toString());
		
		result = new Proc.ProcBuilder("bashCaller","bash", "-c",  "echo $VAR $VAR2")
		.withVar("VAR", "ZHAO").withVar("VAR2", "JIANG").run();
		System.out.println(result);
		
		ByteArrayInputStream input = new ByteArrayInputStream("Hello cruel World".getBytes());

		result = new Proc.ProcBuilder("wcCaller","wc")
		        .withArg("-w")
		        .withInputStream(input).run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("catCaller","cat")
        .withInput("Hello world!").run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("bashCaller","bash")
		.withArg("-c").withArgs("ls . | wc -l")
        .run();
		System.out.println(result);
		
		
		result = new Proc.ProcBuilder("pwdCaller","pwd")
		.workingDirectory("/kla/klaS/sxlink")
        .run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("nosuchcmdCaller","pd")
		.workingDirectory("/kla/klaS/sxlink")
        .run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("catCaller","cat")
        .withArg("txt.txt").run();
		System.out.println(result);
		
		result = new Proc.ProcBuilder("sleepCaller","sleep")
        .withArg("2").withTimeOut(1000).run();
		System.out.println(result);
		
		
	}
	
	

}
