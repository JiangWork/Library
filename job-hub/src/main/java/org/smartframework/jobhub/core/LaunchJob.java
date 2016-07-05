package org.smartframework.jobhub.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.smartframework.jobhub.common.ProgressReporter;
import org.smartframework.jobhub.common.Progressable;
import org.smartframework.jobhub.core.support.DefaultProgressReporter;

/**
 * Launch the job started by separated JVM.
 * @author Miller
 * @date Jul 3, 2016 7:47:36 PM
 */

public class LaunchJob {
	
	private final static Logger logger = Logger.getLogger(LaunchJob.class);
	
	public static void main(String[] args)  {
		if (args.length < 4) {
			System.err.println("needs three args: hostname port job-config logpath");
			System.exit(1);
		}
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		String configPath = args[2];
		String logPath = args[3];
		configureLog(logPath);
		logger.info("Input parameters: hostName=" + hostName +  " port=" 
				+ port + " config=" + configPath + " logpath=" + logPath);
		
		JobDefinition def = new JobDefinition();
		try {
			def.read(configPath);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			logger.error(e.getMessage(), e);
			System.exit(2);
		}
		int pid = Integer.parseInt(System.getenv("PID"));
		logger.info("Job config: pid=" + pid + " jobName=" + def.getJobName() + " submitter=" + def.getSubmitter());
		String mainClass = def.getMainClass();
		try {
			Class<?> clazz =  Class.forName(mainClass);
			Object obj = clazz.newInstance();
			// set the interfaces
			setInterfaces(obj, def);

			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			logger.error(e.getMessage(), e);
			System.exit(3);
		}
	}
	
	public static void configureLog(String logPath) {
		Properties pro = new Properties();
        pro.put("log4j.rootLogger", "DEBUG,C,R");

        pro.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
        pro.put("log4j.appender.C.Threshold", "INFO");
        pro.put("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.C.layout.ConversionPattern", "%n %m");

        pro.put("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
        pro.put("log4j.appender.R.File", logPath);
        pro.put("log4j.appender.R.MaxFileSize", "10000KB");
        pro.put("log4j.appender.R.MaxBackupIndex", "5");
        pro.put("log4j.appender.R.Threshold", "DEBUG");
        pro.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.R.layout.ConversionPattern", "%n[%d{HH:mm:ss}] [%p] %m");
        
		PropertyConfigurator.configure(pro);
	}

	public static void setInterfaces(Object obj, JobDefinition def) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// set the progress reporter
		if (obj instanceof Progressable) {
			String reporterClass = def.getProperty("job.reporter.class");
			if (reporterClass == null) {
				reporterClass = DefaultProgressReporter.class.getName();
			}
			Class<?> clazz = Class.forName(reporterClass);
			if (!ProgressReporter.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException(reporterClass + " is not a subclass of " + ProgressReporter.class.getName());
			}
			Progressable progressable = (Progressable) obj;
			progressable.setReporter((ProgressReporter)clazz.newInstance());
		}
	}
	
	public static void invokeMethod(Object obj, JobDefinition def) {
		Class<?> clazz = obj.getClass();
		String methodName = def.getEnterMethod();
		String methodArgs = def.getEnterArgs();
		Method[] methods = clazz.getDeclaredMethods();
		Method candidate = null;
		for (Method method: methods) {
			if(method.getName().equals(methodName)) {
				if(candidate == null) {
					candidate = method;
				} else {
					throw new IllegalArgumentException("More than one methods are found:" + methodName);
				}
			}
		}
		
	}
	

}
