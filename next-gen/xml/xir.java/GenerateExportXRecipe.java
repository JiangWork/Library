package com.klatencor.klara.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.klatencor.klara.common.enc.Blowfish;
import com.klatencor.klara.common.handler.SLXFileLoader;
import com.klatencor.klara.common.ktutil.KTUtil;
import com.klatencor.klara.common.objlayerSLX.Inspection;
import com.klatencor.klara.common.objlayerSLX.Recipe;

public class GenerateExportXRecipe
{

	private Logger logger = Logger.getLogger(GenerateExportXRecipe.class);
	private String recipeName = null;
	private String exportXRecipeFileName = null;
	private int plateAlignImagesSelected = 0;

	private Template template = null;
	private VelocityContext context = null;
	private Recipe recipe = null;
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
	
	public GenerateExportXRecipe(String recipeName,String exportPrintIrFileName,int plateAlignImagesSelected )
	{
		this.recipeName = recipeName;
		this.exportXRecipeFileName = exportPrintIrFileName;
		this.plateAlignImagesSelected = plateAlignImagesSelected;
		
	}
	
	private VelocityContext getVelocityContext() throws Exception
    {
		VelocityContext context = new VelocityContext();
		
		
		ExportXRecipeUtil exportXRecipeUtil = null;
		
    	try
    	{
    		recipe = getRecipe(recipeName);
    		exportXRecipeUtil =  new ExportXRecipeUtil(recipe);
    		context.put("recipe", recipe);
    		context.put("util", exportXRecipeUtil);
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

		template  = Velocity.getTemplate("xr_recipe.vm");
		context = getVelocityContext();
		StringWriter sw = new StringWriter();
		template.merge(context,sw );
		FileOutputStream fos = new FileOutputStream(exportXRecipeFileName,false);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeBytes(sw.toString());
		dos.flush();
		dos.close();
	}
	public Recipe getRecipe(String recipeFileName) throws Exception
	{
		Recipe recipe = null;
		FileInputStream fis=null;
		BufferedInputStream bis=null;
		DataInputStream dis = null;
		try
		{
			 fis = new FileInputStream(recipeFileName);
			 bis = new BufferedInputStream(fis);
			 dis = new DataInputStream(bis);
			 recipe = new Recipe(dis,new Blowfish());
			
		}
		catch (Exception e) 
		{
			System.out.println("Read Error Recipe File Name : "+recipeFileName);
			logger.error("Read Error Recipe File Name : "+recipeFileName);
			logger.error("Read Error Message : "+e.getMessage());
		}
		finally
		{
			if(dis != null )
			{
				dis.close();
			}
			if(fis != null)
			{
				fis.close();	
			}
			
		}
		return recipe;
	}
	public String processTagsInFileName(String fileName) throws Exception
	{
		fileName = fileName.replaceAll("%#", recipe.getFmId()+"");
		fileName = fileName.replaceAll("%RCP", recipe.getSetName());
		Calendar currentCalendar = Calendar.getInstance();
		String currentDateStr = KTUtil.toString(currentCalendar, "dd-MMM-yyyy_HH:mm:ss");
		fileName = fileName.replaceAll("%D", currentDateStr);
		fileName = fileName.replaceAll("%S", System.currentTimeMillis()+"");
		return fileName;
	}
	public String getEmailSubjectLine() throws Exception
	{
		String subject ="";
		if(recipe != null)
		{	
			subject = "FmId : "+recipe.getFmId() + " RecipeName : "+recipe.getSetName();
		}	
		return subject;
	}
	public String getFmId()
	{
		String fmid = "";
		if(recipe != null)
		{
			fmid = recipe.getFmId()+"";
		}
		return fmid;
	}

}
