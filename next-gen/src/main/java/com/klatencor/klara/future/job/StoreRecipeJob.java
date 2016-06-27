package com.klatencor.klara.future.job;

import java.io.File;

import com.klatencor.klara.future.dao.RecipeDao;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.parser.CastorXmlParser;
import com.klatencor.klara.future.support.FeInvoker;
import com.klatencor.klara.future.utils.IOUtils;

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
		JobParameters parameters = getParameters();
		JobResult result = JobResult.newSuccess();	
		this.setJobResult(result);
		String rcpPath = parameters.getString("recipePath");
		report("loading file: " + rcpPath);
		File file = new File(rcpPath);
		if (!file.canRead()) {
			result.fail("can't read file:" + file.getAbsolutePath());
			report("can't read file:" + file.getAbsolutePath());
			return;
		}
		String xmlPath  = IOUtils.tempFileName(4);
		if (!FeInvoker.generateXml(rcpPath, xmlPath)) {
			result.fail("can't generate the xml for :" + rcpPath + ", see log for details, rcpVersion=" + IOUtils.getVersion(rcpPath));
			report("can't generate the xml for :" + rcpPath);
			return;
		}
		CastorXmlParser<Recipe> parser = new CastorXmlParser<Recipe>("recipe-mapping.xml");
		Recipe recipe = parser.parse(xmlPath + ".xml");
		report("load XML into Object successfully.");
		recipe.setRecipeType("S");
		recipe.setRecipeStoragePath(rcpPath);
		recipe.readHeader(rcpPath);
		report("####Recipe Plate type: "+recipe.getPlateType());
		RecipeDao recipeDao = new RecipeDao();		
		JobResult daoResult = recipeDao.storeRecipe(recipe, parameters);
		if (!daoResult.isStatus()) {
			result.fail(daoResult.getReason());
			report("fail to add to DB:" + daoResult.getReason());
			return;
		} else {
			report("load recipe into DB successfully.");
		}
		// setting results
		result.addResult("fmid", ""+recipe.getFmId());
		result.addResult("ppid", ""+recipe.getPpId());
		result.addResult("setname", recipe.getRecipeGeneral().getRecipeName());
	}

	@Override
	public void clean() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
