package com.klatencor.klara.future.job;

import com.klatencor.klara.future.dao.RecipeDao;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.parser.CastorXmlParser;
import com.klatencor.klara.future.server.Server;
import com.klatencor.klara.future.server.ServerContext;

public class LoadRecipeJob extends Job {

	private String fileName;
	
	
	public LoadRecipeJob(long jobId) {
		this(jobId, null);
	}
	
	public LoadRecipeJob(long jobId, Reporter reporter) {
		this.setJobName("loadRecipe");
		this.setJobId(jobId);
		this.setReporter(reporter);
	}
	
	@Override
	public void setup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() throws Exception {
		doReport("loading file: " + fileName);
		
		String xmlPath  = "";
		CastorXmlParser<Recipe> parser = new CastorXmlParser<Recipe>("recipe-mapping.xml");
		Recipe recipe = parser.parse(xmlPath);
		
		RecipeDao recipeDao = new RecipeDao();
		
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
