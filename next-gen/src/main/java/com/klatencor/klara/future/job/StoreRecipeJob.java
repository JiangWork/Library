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
		
		RecipeDao recipeDao = new RecipeDao();
		getParameters();
		
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
