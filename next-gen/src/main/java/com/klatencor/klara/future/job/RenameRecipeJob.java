package com.klatencor.klara.future.job;

import java.io.File;

import com.klatencor.klara.future.support.RecipeOperator;

/**
 * Rename the recipe name via invoking FE shared libraries.
 * 
 * @author jiangzhao
 * @date  Jun 29, 2016
 * @version V1.0
 */
public class RenameRecipeJob extends DefaultJob {

	public RenameRecipeJob(long jobId) {
		super(jobId, "RenameRecipeJob", null);
	}
	
	@Override
	public void execute() throws Exception {
		JobParameters parameters = getParameters();
		JobResult result = JobResult.newSuccess();
		setJobResult(result);
		String recipePath = parameters.getString("recipe.path");
		String newName = parameters.getString("recipe.newname");
		
		File recipeFile = new File(recipePath);
		if (!recipeFile.exists()) {
			report("recipe doesn't exist: " + recipePath);
			result.fail("recipe doesn't exist: " + recipePath);
			return;
		}
		RecipeOperator op = new RecipeOperator(recipePath);
		report("loading recipe using JniRecipeOperator: " + recipePath);
		if(!op.open()) {
			report("loading recipe error");
			result.fail("loading recipe error");
			op.close();
			return;
		}
		String newPath = recipeFile.getParentFile().getAbsolutePath() + File.pathSeparator + newName;
		if (!op.write(newPath)) {
			report("write recipe error");
			result.fail("write recipe error");
			op.close();
			return;
		}
		op.close();
		result.addResult("recipe.path", newPath);
		report("Successfully rename the recipe, new recipe is located at: " + newPath);
	}

}
