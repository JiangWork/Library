package com.klatencor.klara.future.job;

import com.klatencor.klara.future.dao.RecipeDao;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.parser.CastorXmlParser;

public class StoreRecipeJob extends DefaultJob {

	private String recipePath;
	
	
	public StoreRecipeJob(long jobId) {
		this(jobId, null);
	}
	
	public StoreRecipeJob(long jobId, Reporter reporter) {
		super(jobId, "loadRecipe", reporter);
	}
	
	@Override
	public void setup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void execute() throws Exception {
		report("loading file: " + recipePath);
		
		String xmlPath  = "";
		// invoke FE executable
		CastorXmlParser<Recipe> parser = new CastorXmlParser<Recipe>("recipe-mapping.xml");
		Recipe recipe = parser.parse(xmlPath);
		
		JobParameters parameters = getParamters();
		
		recipe.setRecipeType("S");
		recipe.setRecipeStoragePath(recipePath);
		recipe.setSystemName(parameters.getString("systemName"));
		
		RecipeDao recipeDao = new RecipeDao();
		
		
		JobResult result = recipeDao.storeRecipe(recipe, parameters.getBoolean("newOrUpdate"), 
				parameters.getInt("fmid"));
		
		
		
		int count = parameters.getInt("max.count");
		
		
		
		result.setStatus(false);
		result.setReason("Ouch, something is wrong.");
		this.setJobResult(result);
		
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
