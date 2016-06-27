package com.klatencor.klara.future.job;

import com.klatencor.klara.future.support.FeInvoker;

public class GenerateXmlJob extends DefaultJob{

	public GenerateXmlJob(long jobId) {
		this(jobId, null);
	}
	
	public GenerateXmlJob(long jobId, Reporter reporter) {
		super(jobId, "generateXml", reporter);
	}
	
	
	@Override
	public void setup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void execute() throws Exception {
		JobParameters parameters = getParameters();
		String rcpPath = parameters.getString("inPath");
		String xmlPath = parameters.getString("outPath");
		
		JobResult result = JobResult.newSuccess();	
		this.setJobResult(result);
		
		xmlPath = xmlPath.replaceAll("\\.xml", ""); //remove extension after the file name.
		if(FeInvoker.generateXml(rcpPath, xmlPath)){
			report("Generate XML file successfully under the path: " + xmlPath + ".xml");
		}
		else{
			result.fail("See logs for details");
			report("Failed to generate the XML file:  " + xmlPath + ".xml");
		}
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
