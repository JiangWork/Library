package library.web.dispatcher.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.log4j.Logger;

public class WordCountSubmitter {

	private final static Logger logger = Logger.getLogger(WordCountSubmitter.class);
	private Map<String, Job> submittedJobs = new HashMap<String, Job>();
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text(); 

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, 
				Context context
				) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
//      ##### configuration when run in Eclipse		
		conf.addResource("classpath:/core-site.xml");
//		Following line uses code to configure. 
//		conf.set("fs.default.name", "hdfs://localhost:8020");
		Job job = Job.getInstance(conf, "word count");
	    
//		Following line is used when jar is available
//		job.setJarByClass(WordCount.class);
//		Following line is used when run in Eclipse
	    job.setJar("/Users/Miller/Work/JavaCode/SmartFramework/target/smart-framework.jar");
	    job.setMapperClass(TokenizerMapper.class);
	    job.setCombinerClass(IntSumReducer.class);
	    job.setReducerClass(IntSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    
	    Path outPath = new Path("output-2");
	    FileSystem fs = outPath.getFileSystem(conf);
	    if (fs.exists(outPath))
	    {
	    	logger.info(outPath.toUri() + " exists, delete it.");
	    	if (fs.delete(outPath, true)) {
	    		logger.info(outPath.toUri() + " successfully delete.");
	    	}
	    }
	    
	    FileInputFormat.addInputPath(job, new Path("/user/jiazhao/demo-input/hadoop"));
	    FileOutputFormat.setOutputPath(job, new Path("output-2"));
	    boolean status = job.waitForCompletion(true);
	    if (status) {
	    	System.out.println(readFiles(outPath, fs));
	    } else {
	    	JobStatus jobStatus = job.getStatus();
	    	System.err.println(String.format("Job %s fails.", jobStatus.getJobID()));
	    }
	    
	    
	}

	private Job submitInternal(String inputDirectory, String outputDirectory) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.addResource("classpath:/core-site.xml");  // in order to get FileSystem
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(WordCountSubmitter.class);
	    job.setMapperClass(TokenizerMapper.class);
	    job.setCombinerClass(IntSumReducer.class);
	    job.setReducerClass(IntSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    
	    Path outPath = new Path(outputDirectory);
	    Path inPath = new Path(inputDirectory);
	    FileSystem fs = outPath.getFileSystem(conf);
	    if (fs.exists(outPath))
	    {
	    	logger.info(outPath.toUri() + " exists, delete it.");
	    	if (fs.delete(outPath, true)) {
	    		logger.info(outPath.toUri() + " successfully delete.");
	    	}
	    }
	    
	    if (!fs.exists(inPath)) {
	    	throw new IOException("Input doesnot exist: " + inputDirectory);
	    }
	    FileInputFormat.addInputPath(job, inPath);
	    FileOutputFormat.setOutputPath(job, outPath);
	    job.submit();
	    return job;
	}
	
	/**
	 * Submit and return the JobId+TrackingURL immediately.
	 * @param inputDirectory
	 * @param outputDirectory
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public String submit(String inputDirectory, String outputDirectory) throws IOException, ClassNotFoundException, InterruptedException {
		Job job = submitInternal(inputDirectory, outputDirectory);
		String jobId = job.getJobID().toString();
		submittedJobs.put(jobId, job);
		return String.format("JobId: %s\nTracking URL:%s", jobId, job.getTrackingURL());
	}
	
	public String submitAndWait(String inputDirectory, String outputDirectory) throws IOException, ClassNotFoundException, InterruptedException {
		Job job = submitInternal(inputDirectory, outputDirectory);
	    boolean status = job.waitForCompletion(true);
	    Path outPath = new Path(outputDirectory);
	    FileSystem fs = outPath.getFileSystem(job.getConfiguration());
	    if (status) {
	    	return readFiles(outPath, fs);
	    } else {
	    	JobStatus jobStatus = job.getStatus();
	    	return String.format("Job %s fails: %s", jobStatus.getJobID(), jobStatus.getFailureInfo());
	    }
	}
	
	public String query(String jobId) throws IOException {
		if (submittedJobs.containsKey(jobId)) {
			Job job = submittedJobs.get(jobId);
			String report = 
			        (" map " + StringUtils.formatPercent(job.mapProgress(), 0)+
			            " reduce " + 
			            StringUtils.formatPercent(job.reduceProgress(), 0));
			return report;
		} else {
			return "No such jobId:" + jobId;
		}
	}
	
	// read all contents under directory
	public static String readFiles(Path directory, FileSystem fs) throws FileNotFoundException, IOException {
		StringBuilder sb = new StringBuilder();
		RemoteIterator<LocatedFileStatus> iter = fs.listFiles(directory, true);
		while(iter.hasNext()) {
			LocatedFileStatus fstatus = iter.next();
			if (fstatus.isFile()) {
				sb.append(readFile(fstatus.getPath(), fs));
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	public static String readFile(Path file, FileSystem fs) throws IOException {
		StringBuilder sb = new StringBuilder();
		FSDataInputStream is = fs.open(file);
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	String inLine = null;
    	while((inLine = br.readLine()) != null) {
    		sb.append(inLine);
    		sb.append('\n');
    	}
    	br.close();
    	return sb.toString();
	}
}
