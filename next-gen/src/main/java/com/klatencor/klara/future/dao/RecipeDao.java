package com.klatencor.klara.future.dao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.klatencor.klara.future.job.JobResult;
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
	

	public JobResult storeRecipe(final Recipe recipe, final boolean newOrUpdate, final int fmId) {
		SQLExecutor executor = Server.getContext().getSqlExecutor();
		final JobResult result = new JobResult();
		result.setStatus(true);
		executor.doTransaction(new DaoCommand<Void>() {

			@Override
			public Void run(JdbcTemplate jdbcTemplate) {
				try {
					insertRecipeHeader(recipe, jdbcTemplate, newOrUpdate, fmId);
					insertInspectionAreas(recipe, jdbcTemplate);
				} catch(Exception e) {
					result.setStatus(false);
					result.setReason("Insert recipe fails due to (rollback): " + e.getMessage());
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
	private void insertRecipeHeader(Recipe recipe, JdbcTemplate jdbcTemplate,  boolean newOrUpdate, int fmId) {
		ArgsBuilder builder = new ArgsBuilder();
		String sql = null;
		long fileSize = (new File(recipe.getRecipeStoragePath())).length();
		int newFmId = 0;
		if (fmId != 0 && !newOrUpdate) { // update
			sql = "UPDATE t_file_master SET FILE_TYPE = ?, SYSTEM_NAME = ?, CHECK_IN_OUT_FLAG = 0	, ARCHIVE_FLAG = 0" +
					", STORAGE_PATH = ?, INSERTED_DATE_TIME = sysdate" +
					", FM_FILE_SIZE = ?, IMAGES_IMPORTED = 'U'" +
					", ALIGNMENT_IMAGES_IMPORTED = ?, SET_NAME = ?, INSPECTION_START_DATETIME = sysdate" +
					", SYSTEM_TYPE_ENUM = ?, STORE_STATUS_ENUM = ?, VERSION = ?, PLATE_SIZE_ENUM = ?" +
					", PLATE_TYPE_ENUM = ?" +
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
			.add(1)  // systemType unknown
			.add(1)
			.add(recipeVesion)
			.add(1)   // plateSize, unknown
			.add(1)  // plateType, unknown
			.add(fmId);
			Object[] args = builder.buildArray();
			jdbcTemplate.update(sql, args);
			recipe.setFmId(fmId);
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
					", PLATE_SIZE_ENUM, PLATE_TYPE_ENUM, GMT_OFFSET )" +
					" VALUES" +
					"( ?, ?, ?, ?, 0, 0, ?, sysdate" +
					", ?, 'U', ?, S_PROC_PGM_KLARA.nextval, sysdate, ?, ?" +
					", ?, ?, ?, 0)";
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
				.add(1)    // systemType unknown
				.add(1)    // STORE_STATUS_ENUM
				.add(recipeVesion)
				.add(1)   // plateSize, unknown
				.add(1);  // plateType, unknown
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
			.add("") // getSensLevelString unknown
			.add("") // getUPALevelString unknown
			.add("") // getSeverityString unknown
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
	
	
}
