package com.klatencor.klara.future.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import com.klatencor.klara.future.job.JobParameters;
import com.klatencor.klara.future.job.JobResult;
import com.klatencor.klara.future.object.MachineType;
import com.klatencor.klara.future.object.MappingLookup;
import com.klatencor.klara.future.object.Version;
import com.klatencor.klara.future.object.recipe.InspectionArea;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.server.Server;

public class RecipeDao {	
	
	private final static String IA_INSERT_SQL = "INSERT INTO T_SLX_RCP_INSPECT_AREA(PP_ID, INSPECTION_AREA_ID, FM_ID, " +
		      "NAME, IA_ID, ALGO, SURFACE_ENUM, SHAPE_ENUM, RADIUS, COPY_FROM_IA, " +
		      "SLC_COPY_FROM_IA , PSC_COPY_FROM_IA, LC_COPY_FROM_IA,  " +
		      "INSPECTION_TYPE_ENUM, DIE_COUNT_X, DIE_COUNT_Y, DIE_SIZE_X, " +
		      "DIE_SIZE_Y, DIE_PITCH_X, DIE_PITCH_Y, CELL_PITCH_X, CELL_PITCH_Y, " +
		      "DIE_POINT_SET_STR, INSPECTION_TYPE_STR,ALGORITHM_DECODED,INSPECTION_TYPE_ENUM_DECODED ) " +
		      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?,?)";
	private final static String SENS_INSERT_SQL  =  "INSERT INTO T_SLX_RCP_IA_SENSITIVITY(SENSITIVITY_ID, INSPECTION_AREA_ID, " +
	        "SENSITIVITY_MODE, X_COORDINATE, Y_COORDINATE, CONCAT_RADIUS, " +
	        "BOOLEAN_OPTIONS, FOCUS_OFFSET, SENSE_LEVEL_STR, UPA_LEVEL_STR, " +
	        "SEVERITY_STR, VIEW_NAME, EDGE_SPEED_RATIO ) " +
	         "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	

	public JobResult storeRecipe(final Recipe recipe, final JobParameters parameters) {
		SQLExecutor executor = Server.getContext().getSqlExecutor();
		final JobResult result = JobResult.newSuccess();
		executor.doTransaction(new DaoCommand<Void>() {

			@Override
			public Void run(JdbcTemplate jdbcTemplate) {
				try {
					insertRecipeHeader(recipe, jdbcTemplate, parameters);
					insertProcessProgram(recipe, jdbcTemplate, false);
				    insertSLXRecipe(recipe, jdbcTemplate);
				    insertBehavior(recipe, jdbcTemplate);
					insertInspectionAreas(recipe, jdbcTemplate);
					writeFmIdToIR(recipe);
					updateProcessProgram(recipe, jdbcTemplate);
				} catch(Exception e) {
					result.setStatus(false);
					result.setReason("insert recipe fails due to (rollback): " + e.getMessage());
					throw new RuntimeException(e.getMessage(), e);
				} 
				return null;
			}
			
		});		
		return result;
	}
	
	/**
	 * corresponding to recipe@kla90InsertRecipe
	 * @param jdbcTemplate
	 */
	private void insertRecipeHeader(Recipe recipe, JdbcTemplate jdbcTemplate, JobParameters parameters) {
		ArgsBuilder builder = new ArgsBuilder();
		String sql = null;
		long fileSize = (new File(recipe.getRecipeStoragePath())).length();
		int fmId = parameters.getInt("fmid");
		boolean newOrUpdate = parameters.getBoolean("newOrUpdate");
		int newFmId = 0;
		if (fmId != 0 && !newOrUpdate) { // update
			sql = "UPDATE t_file_master SET FILE_TYPE = ?, SYSTEM_NAME = ?, CHECK_IN_OUT_FLAG = 0	, ARCHIVE_FLAG = 0" +
					", STORAGE_PATH = ?, INSERTED_DATE_TIME = sysdate" +
					", FM_FILE_SIZE = ?, IMAGES_IMPORTED = 'U'" +
					", ALIGNMENT_IMAGES_IMPORTED = ?, SET_NAME = ?, INSPECTION_START_DATETIME = sysdate" +
					", SYSTEM_TYPE_ENUM = ?, STORE_STATUS_ENUM = ?, VERSION = ?, PLATE_SIZE_ENUM = ?" +
					", PLATE_TYPE_ENUM = ?, PLATE_TYPE_NAME = ?" +
					"WHERE FM_ID = ?" ;
			int recipeVesion = Integer.parseInt(recipe.getRecipeGeneral().getRecipeVersion());
			if (recipeVesion > Version.V600 ) {
				builder.add("6XX_RECIPE");
			} else if (recipeVesion > Version.V206 ) {
				builder.add("TeraScan_RECIPE");
			} else if (recipeVesion > Version.V100 &&  recipeVesion<= Version.V206) {
				builder.add("SLF_RECIPE");
			} else if (recipeVesion<= Version.V100) {
				builder.add("SL3_RECIPE");
			} else {
				builder.add("UNKNOWN_RECIPE");
			}
			String systemName = recipe.getSystemName();
			if (systemName == null) {
				systemName = "RSTR";
				systemName += new SimpleDateFormat("MMddyyhhmmss").format(Calendar.getInstance().getTime());
			}
			builder.add(systemName)
			.add(recipe.getRecipeStoragePath())
			.add(fileSize)
			.add("Y")  // unknown
			.add(recipe.getRecipeGeneral().getRecipeName())
			.add(recipe.getSystemType()) 
			.add(1)
			.add(recipeVesion)
			.add(recipe.getPlateType())   // plateSize, unknown
			.add(recipe.getPlateSize())  // plateType, unknown
			.add(recipe.getRecipeGeneral().getPlateInfo().getPlateType())
			.add(fmId);
			Object[] args = builder.buildArray();
			jdbcTemplate.update(sql, args);
			recipe.setFmId(fmId);
			jdbcTemplate.execute("DELETE from t_process_program_klara where fm_id = "+ fmId);
			int ppId = jdbcTemplate.queryForInt( "select PP_ID FROM T_FILE_MASTER where FM_ID=?", fmId);
			recipe.setPpId(ppId);
			
		} else {
			if (fmId == 0) {
				sql = "select fm_id.nextval FROM DUAL"; 
				newFmId = jdbcTemplate.queryForInt(sql);
			} else if(fmId != 0 && newOrUpdate) {
				newFmId = fmId;
			}
			sql = "INSERT INTO T_FILE_MASTER" +
					"( FM_ID, FILE_TYPE, SYSTEM_NAME, SET_NAME"+
					", CHECK_IN_OUT_FLAG, ARCHIVE_FLAG, STORAGE_PATH, INSERTED_DATE_TIME" +
					", FM_FILE_SIZE, IMAGES_IMPORTED, ALIGNMENT_IMAGES_IMPORTED, PP_ID" +
					", INSPECTION_START_DATETIME, SYSTEM_TYPE_ENUM, STORE_STATUS_ENUM, VERSION" +
					", PLATE_SIZE_ENUM, PLATE_TYPE_ENUM, GMT_OFFSET, PLATE_TYPE_NAME )" +
					" VALUES" +
					"( ?, ?, ?, ?, 0, 0, ?, sysdate" +
					", ?, 'U', ?, S_PROC_PGM_KLARA.nextval, sysdate, ?, ?" +
					", ?, ?, ?, 0, ?)";
				builder.add(newFmId);
				int recipeVesion = Integer.parseInt(recipe.getRecipeGeneral().getRecipeVersion());
				if (recipeVesion > Version.V600 ) {
					builder.add("6XX_RECIPE");
				} else if (recipeVesion > Version.V206 ) {
					builder.add("TeraScan_RECIPE");
				} else if (recipeVesion > Version.V100 &&  recipeVesion<= Version.V206) {
					builder.add("SLF_RECIPE");
				} else if (recipeVesion<= Version.V100) {
					builder.add("SL3_RECIPE");
				} else {
					builder.add("UNKNOWN_RECIPE");
				}
				String systemName = recipe.getSystemName();
				if (systemName == null) {
					systemName = "RSTR";
					systemName += new SimpleDateFormat("MMddyyhhmmss").format(Calendar.getInstance().getTime());
				}
				builder.add(systemName)
				.add(recipe.getRecipeGeneral().getRecipeName())
				.add(recipe.getRecipeStoragePath())
				.add(fileSize)
				.add("Y")   // alignImagePresent unknown now, need to decide in future
				.add(recipe.getSystemType())    // systemType unknown
				.add(1)    // STORE_STATUS_ENUM
				.add(recipeVesion)
				.add(recipe.getPlateSize())   // plateSize, unknown
				.add(recipe.getPlateType())  // plateType, unknown
				.add(recipe.getRecipeGeneral().getPlateInfo().getPlateType());
				Object[] args = builder.buildArray();
				jdbcTemplate.update(sql, args);
				recipe.setFmId(newFmId);	
				int ppId = jdbcTemplate.queryForInt("select S_PROC_PGM_KLARA.currval FROM DUAL");
				recipe.setPpId(ppId);			
		}
	}

	
	private void insertInspectionAreas(Recipe recipe, JdbcTemplate jdbcTemplate) {
		
		List<InspectionArea> list = recipe.getInspectionAreaList();
		ArgsBuilder builder = new ArgsBuilder();
		for (InspectionArea ia: list) {
			builder.clear();
			int nextId = jdbcTemplate.queryForInt("SELECT S_SLX_RCP_INSPECT_AREA.NEXTVAL from dual");
			builder.add(recipe.getPpId())
			.add(nextId)
			.add(recipe.getFmId())
			.add("unknown")  //name unknown
			.addInt(ia.getId())
			.add(1)  // algorithm unknown
			.add(MappingLookup.surfaceMapping.getEnumIndex(ia.getSurface()))
			.add(MappingLookup.shapeMapping.getEnumIndex(ia.getShape()))
			.addInt(ia.getRadius())
			.add(1)  // copyFromIA unknown
			.add(1)  // slcCopyFromIA unknown
			.add(1)  // pscCopyFromIA unknown
			.add(1)  // lcCopyFromIA unknown
			.add(1)  //inspectionTypeEnumDecoded unknown
			.addInt(ia.getDieCount().getX())
			.addInt(ia.getDieCount().getY())
			.addInt(ia.getDieSize().getX())
			.addInt(ia.getDieSize().getY())
			.addInt(ia.getDiePitch().getX())
			.addInt(ia.getDiePitch().getY())
			.add(0) // cellPitch X unknown
			.add(0) // cellPitch Y unknown
			.add("")  // getDiePointSetString unknown
			.add(ia.getInspectionType())
			.add(ia.getAlgo())
			.add(0); // inspectionTypeEnumDecoded unknown
			jdbcTemplate.update(IA_INSERT_SQL, builder.buildArray());
			// skip inspection addToDBIAPoints and addToDBIAAlgos
			// add sensitivity

			int nextSenseId = jdbcTemplate.queryForInt("SELECT S_SLX_RCP_IA_SENS.NEXTVAL from dual");
			builder.clear();
			builder.add(nextSenseId)
			.add(nextId)
			.add(0)
			.addDouble(ia.getSensitivity().getPixelSize().getX())
			.addDouble(ia.getSensitivity().getPixelSize().getY())
			.addDouble(ia.getSensitivity().getConcatRadius())
			.add(ia.getSensitivity().getOpcEnable().equals("True")?1:0)
			.add(0)  //focusOffset unknown
			.add("unknown") // getSensLevelString unknown
			.add("unknown") // getUPALevelString unknown
			.add("unknown") // getSeverityString unknown
			.add(ia.getSensitivity().getAcquisitionView())
			.addDouble(ia.getSensitivity().getEdgeSpeedRatio());
			jdbcTemplate.update(SENS_INSERT_SQL, builder.buildArray());
			// skip sensitivity flex knob, printFilter
			// skip lightCal
			// skip starlightCal
			// skip preSwathCalibration
			// skip senseRegion
			// skip totalPlateQuality
			
		}
	}
	
	private void insertProcessProgram(Recipe recipe, JdbcTemplate jdbcTemplate, boolean commit ) throws Exception
	  {
		ArgsBuilder builder = new ArgsBuilder();  
		if(recipe.getMachineType() > 3 )
		  {
			  String sql = "INSERT INTO T_PROCESS_PROGRAM_KLARA( PP_ID, FM_ID, MACHINE_TYPE, SET_NAME" +
			  ", LEVEL_NAME, PROGRAM_NAME, PROGRAM_REVISION " +
			  ", SYSTEM_TYPE_ENUM, RECIPE_DATE_TIME, RECIPE_TYPE, STORE_STATUS_ENUM" +
			  " )VALUES" +
			  "( ?, ?, ?, ?" +
			  ", ?, ?, ?" +
			  ", ?, sysdate, ?, ? )"; 
			  
				  
			  builder.add(recipe.getPpId())
			  .add(recipe.getFmId())
			  .add(recipe.getMachineType())
			  .add(recipe.getRecipeGeneral().getRecipeName())
			  .add("") //unknown levelName
			  .add("") //unknown programName
			  .add(0) //unknown programRevision
			  .add(recipe.getSystemType())
			  .add(recipe.getRecipeType())
			  .add(1); // unkonwn store status
			  
			  Object[] args = builder.buildArray();
			  jdbcTemplate.update(sql, args);

		  }
		  else
		  {

			  // get a new ID for the Process Program only if the current id has not been
			  // set.fmId
			  int tmp = 0;
			  String sql = "select PP_ID from T_PROCESS_PROGRAM_KLARA " +
	                  "where FM_ID = " + recipe.getFmId();
			  tmp = jdbcTemplate.queryForInt(sql);
			  // Check for the validity of fmId. For SLX it has to be set. For 2XX and 3XX
			  // it does not have to be set.
			  if  (recipe.getMachineType() == MachineType.SLX && recipe.getFmId() == 0)
				  throw new Exception("FM ID has not been set");
			  if  (recipe.getMachineType() == MachineType.FIVE_XX && recipe.getFmId() == 0)
				  throw new Exception("FM ID has not been set");
			  if  (recipe.getMachineType() == MachineType.SIX_XX && recipe.getFmId() == 0)
				  throw new Exception("FM ID has not been set");
			  
			  // add it to the database.
			  sql = "UPDATE T_PROCESS_PROGRAM_KLARA SET " +
			  "LEVEL_NAME=?," +
			  "PROGRAM_NAME =?," +
			  "PROGRAM_REVISION =?  " +
			  "WHERE FM_ID=?" +
			  "and PP_ID =?";
			  // insert values into the table
			  builder.add("") //unknown level name
			  .add("") //unknown program name
			  .add(0) //unknown program revision
			  .add(recipe.getFmId())
			  .add(tmp);

			  Object[] args = builder.buildArray();
			  jdbcTemplate.update(sql, args);
		  }
	  }
	
	private void insertSLXRecipe(Recipe recipe, JdbcTemplate jdbcTemplate) throws IOException{
      String insertQuery ="";
	  String isOneXImage = "true";
	  Properties prop = new Properties();
	  FileInputStream inFile = new FileInputStream("/klaL/logs/config.prop");
      prop.load(inFile);
      inFile.close();
      isOneXImage = prop.getProperty("one_x_image", "true");
	  ArgsBuilder builder = new ArgsBuilder();
	  if(isOneXImage.equalsIgnoreCase("true"))
	  {
	      insertQuery = "INSERT INTO T_SLX_RECIPE( PP_ID, FM_ID, MAGIC_COOKIE, VERSION_NUMBER" +
	      ", RESERVED_WORD_1, RESERVED_WORD_2, NAME, USER_NOTE" +
	      ", PLATE_TYPE_ENUM, PLATE_SIZE_ENUM, PLATE_THICKNESS, PELLICLE_HEIGHT" +
	      ", MIN_VERSION, GOLDEN_IMAGE_FILENAME, IR_FILENAME, LASER_TYPE_ENUM" +
	      ", DATA_BASE_PATH, PLATE_ORIENTATION, TONE_CONTROL, IA_LIST" +
	      ", RCP_WRITER_VERSION, HOST_SYSTEM, DATA_STORED_MAGIC_COOKIE, DATA_STORED_VERSION" +
	      ", DATA_STORED_SECONDS, DATA_STORED_MICRO_SECONDS, DATA_STORED_DISPLAY_ZONE, DATA_STORED_GMT_OFFSET" +
	      ", PLATE_TYPE_MATERIAL_NAMES, PLATE_TYPE_STR, PLATE_OVERVIEW_IMAGES, BBM_SIZE" +
	      ", RTE_BOX_SIZE, RTE_SENSE, RTE_STATE ) values" +
	      
	      "( ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, empty_blob(), ?, ?, ?, ? )" ;
	  }
	  else
	  {
	      insertQuery = "INSERT INTO T_SLX_RECIPE( PP_ID, FM_ID, MAGIC_COOKIE, VERSION_NUMBER" +
	      ", RESERVED_WORD_1, RESERVED_WORD_2, NAME, USER_NOTE" +
	      ", PLATE_TYPE_ENUM, PLATE_SIZE_ENUM, PLATE_THICKNESS, PELLICLE_HEIGHT" +
	      ", MIN_VERSION, GOLDEN_IMAGE_FILENAME, IR_FILENAME, LASER_TYPE_ENUM" +
	      ", DATA_BASE_PATH, PLATE_ORIENTATION, TONE_CONTROL, IA_LIST" +
	      ", RCP_WRITER_VERSION, HOST_SYSTEM, DATA_STORED_MAGIC_COOKIE, DATA_STORED_VERSION" +
	      ", DATA_STORED_SECONDS, DATA_STORED_MICRO_SECONDS, DATA_STORED_DISPLAY_ZONE, DATA_STORED_GMT_OFFSET" +
	      ", PLATE_TYPE_MATERIAL_NAMES, PLATE_TYPE_STR, BBM_SIZE" +
	      ", RTE_BOX_SIZE, RTE_SENSE, RTE_STATE ) values" +
	      
	      "( ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, ?, ?" +
	      ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" ;
	  }
	  int recipeVersion = Integer.parseInt(recipe.getRecipeGeneral().getRecipeVersion());
	  builder.add(recipe.getPpId())
	  .add(recipe.getFmId())
	  .add(0) //unknown magicCookie
	  .add(recipeVersion)
	  .add(0) //unknown reserved word
	  .add(0); //unknown reserved word
	  
	  String rcpName = recipe.getRecipeGeneral().getRecipeName();
	  if(rcpName.length() < 1)
		  rcpName ="<EMPTY>";
	  builder.add(recipe.getRecipeGeneral().getRecipeName())
	  .add(recipe.getRecipeGeneral().getNote())
	  .add(recipe.getPlateType())
	  .add(recipe.getPlateSize()) 
	  .add(0)
	  .add(0)
	  .add(Integer.parseInt(recipe.getRecipeGeneral().getMinimumRecpeVersion()))
	  .add("") //unknown golden image
	  .add("") //unknown ir file name
	  .add(0) //unknown laser type
	  .add(recipe.getRecipeGeneral().getDatabaseName())
	  .add(0) //unknown plate orientation
	  .add(0) //unknown plate tone control
	  .add(recipe.getInspectionAreaList().size())
	  .add("") //unknown writer
	  .add(recipe.getSystemName())
	  .add(0) //data stored magic cookie
	  .add(0) //data stored version
	  .add(recipe.getRcpStoredSeconds())
	  .add(recipe.getRcpStoredMicroSeconds())
	  .add(recipe.getRcpStoredDisplayZone())
	  .add(recipe.getRcpStoredGmtOffset())
	  .add("")
	  .add(recipe.getRecipeGeneral().getPlateInfo().getPlateType())
	  .add(0) //bbmSize
	  .add(0) //rTEboxSize
	  .add(0) //rTEsense
	  .add(0); //rTEstate
	 
	  Object[] args = builder.buildArray();
	  jdbcTemplate.update(insertQuery, args);

	}
	
	private void insertBehavior(Recipe recipe, JdbcTemplate jdbcTemplate){

	      String sql = "INSERT INTO T_SLX_RCP_BEHAVIOR (PP_ID, AUTO_SETUP_BOOL , " +
	        "AUTO_INSPECT_BOOL, AUTO_REVIEW_BOOL, AUTO_UNLOAD_BOOL, " +
	        "ABORT_JOB_ON_ERROR_BOOL, PRINT_AFTER_INSPECTION_BOOL, " +
	        "PRINT_AFTER_REVIEW_BOOL, PRINT_FULL_REPORT_BOOL, " +
	        "STORE_AFTER_INSPECTION_BOOL, STORE_AFTER_REVIEW_BOOL , " +
	        "STORE_IMAGES_ENUM, INSPECTION_COUNT, DISPLAY_NEW_DEFECTS_BOOL, " +
	        "ABORT_FAIL_COUNT, ABORT_WARN_COUNT, Z_CAL_FOR_P90, " +
	        "IAD_DEFECT_CONCAT, AUTO_DNIR, Z_CAL_FOR_P125 ) " +
	        " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	      



	      ArgsBuilder builder = new ArgsBuilder();
	      
	      builder.add(recipe.getPpId());
	      builder.add(recipe.getBehavior().getAutoSetup().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getAutoInspect().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getAutoReview().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getAutoUnload().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getAbortJobOnError().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getPrintAfterInspection().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getPrintAfterReview().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getPrintFullReport().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getStoreAfterInspection().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getStoreAfterReview().trim().equals("True") ? 1:0);
	      builder.add(MappingLookup.storeImagesMapping.getEnumIndex(recipe.getBehavior().getStoreImages()));
	      builder.add(Integer.parseInt(recipe.getBehavior().getInspectionCount()));
	      builder.add(recipe.getBehavior().getDisplayNewDefects().trim().equals("True") ? 1:0);
	      builder.add(Integer.parseInt(recipe.getBehavior().getAbortFailCount()));
	      builder.add(Integer.parseInt(recipe.getBehavior().getAbortWarnCount()));
	      builder.add(recipe.getBehavior().getZCalForP90().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getIadDefectConcat().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getAutoDnir().trim().equals("True") ? 1:0);
	      builder.add(recipe.getBehavior().getZCalForP125().trim().equals("True") ? 1:0);

	      Object[] args = builder.buildArray();
		  jdbcTemplate.update(sql, args);
	    
	  
	}
	
	private void writeFmIdToIR(Recipe recipe)
	{
		RandomAccessFile file = null;
		try
		{
			file = new RandomAccessFile(recipe.getRecipeStoragePath(), "rwd");
			file.readInt(); //Magic cookie
			file.readInt(); //version
			file.writeInt(recipe.getFmId());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(file !=  null)
			try
			{
				file.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void updateProcessProgram(Recipe recipe, JdbcTemplate jdbcTemplate){
		String query = "update T_PROCESS_PROGRAM_KLARA " +
				"set STORE_STATUS_ENUM = 3 " +
				"  where fm_id = " + recipe.getFmId();
		jdbcTemplate.update(query);
	}
	
}
