package com.klatencor.klara.future.job;

import java.io.File;

import com.klatencor.klara.future.dao.RecipeDao;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.parser.CastorXmlParser;

public class StoreRecipeJob extends DefaultJob {

	
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
		JobParameters parameters = getParamters();
		JobResult result = JobResult.newSuccess();	
		this.setJobResult(result);
		String rcpPath = parameters.getString("recipePath");
		report("loading file: " + rcpPath);
		File file = new File(rcpPath);
		if (!file.canRead()) {
			result.fail("can't read file:" + file.getAbsolutePath());
			return;
		}
		String xmlPath  = rcpPath;
		// invoke FE executable to generate XML
		CastorXmlParser<Recipe> parser = new CastorXmlParser<Recipe>("recipe-mapping.xml");
		Recipe recipe = parser.parse(xmlPath);
		recipe.setRecipeType("S");
		recipe.setRecipeStoragePath(rcpPath);
		recipe.setSystemName(parameters.getString("systemName"));		
		RecipeDao recipeDao = new RecipeDao();		
		JobResult daoResult = recipeDao.storeRecipe(recipe, parameters.getBoolean("newOrUpdate"), 
				parameters.getInt("fmid"));
		if (!daoResult.isStatus()) {
			result.fail(daoResult.getReason());
			return;
		}
		
		
		// setting results
		int count = parameters.getInt("max.count");
		
		
		
		result.setStatus(false);
		result.setReason("Ouch, something is wrong.");
		
		
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
