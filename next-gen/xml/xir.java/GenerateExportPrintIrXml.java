package com.klatencor.klara.util;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.klatencor.klara.common.handler.SLXFileLoader;
import com.klatencor.klara.common.ktutil.KTUtil;
import com.klatencor.klara.common.objlayerSLX.Inspection;

public class GenerateExportPrintIrXml
{
	private Logger logger = Logger.getLogger(GenerateExportPrintIrXml.class);
	private String irName = null;
	private String exportPrintIrFileName = null;
	private int plateAlignImagesSelected = 0;
	
	private Template template = null;
	private VelocityContext context = null;
	private Inspection insp = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_HH:mm:ss"); 
	static
	{
		try
		{
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.cache","false");
			p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			p.setProperty("userdirective" , "com.klatencor.klara.util.Ifnotnull,com.klatencor.klara.util.Ifnull");
			Velocity.init(p);
		}
		catch(Exception e)
		{
			//Nothing to do
		}
	}
	
	public GenerateExportPrintIrXml(String irName,String exportPrintIrFileName,int plateAlignImagesSelected )
	{
		this.irName = irName;
		this.exportPrintIrFileName = exportPrintIrFileName;
		this.plateAlignImagesSelected = plateAlignImagesSelected;
	}
	
	private VelocityContext getVelocityContext() throws Exception
    {
		VelocityContext context = new VelocityContext();
		
		
		ExportPrintIrUtil exportPrintIrUtil = null;
		
    	try
    	{
    		insp = SLXFileLoader.readFile(this.irName, false, false);
    		exportPrintIrUtil =  new ExportPrintIrUtil(insp);
    		context.put("insp", insp);
    		context.put("util", exportPrintIrUtil);
    		context.put("plateAlignImagesSelected", plateAlignImagesSelected);
    	}
    	catch (Exception e)
    	{
    		logger.info(e,e);
		}
    	return context;
    }
	public void generateFile()throws Exception
	{

		template  = Velocity.getTemplate("exportprintir.vm");
		context = getVelocityContext();
		StringWriter sw = new StringWriter();
		template.merge(context,sw );
		FileOutputStream fos = new FileOutputStream(exportPrintIrFileName,false);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeBytes(sw.toString());
		dos.flush();
		dos.close();
	}
	
	public String processTagsInFileName(String fileName) throws Exception
	{
		fileName = fileName.replaceAll("%#", insp.getFmId()+"");
		fileName = fileName.replaceAll("%PN", insp.getSetName());
		fileName = fileName.replaceAll("%IN", insp.getExtensionName());
		fileName = fileName.replaceAll("%IST", sdf.format(insp.getInspectionStarted().asJavaUtilDate()));
		
		Calendar currentCalendar = Calendar.getInstance();
		String currentDateStr = KTUtil.toString(currentCalendar, "dd-MMM-yyyy_HH:mm:ss");
		fileName = fileName.replaceAll("%D", currentDateStr);
		fileName = fileName.replaceAll("%S", System.currentTimeMillis()+"");
		
		
		
		return fileName;
	}
	public String getEmailSubjectLine() throws Exception
	{
		String subject ="";
		if(insp != null)
		{	
			subject = "FmId : "+insp.getFmId() + " PlateName : "+insp.getSetName()+" InspectionName : "+insp.getExtensionName();
		}	
		return subject;
	}
	public String getFmId()
	{
		String fmid = "";
		if(insp != null)
		{
			fmid = insp.getFmId()+"";
		}
		return fmid;
	}
	
}
