package com.klatencor.klara.future.support;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * A interface to access FE shared library to get or update the
 * information from IR/recipe.
 * 
 * @author jiangzhao
 * @date  Jun 26, 2016
 * @version V1.0
 */
public class JniDataOperations {
	
	private final static Logger logger = Logger.getLogger(JniDataOperations.class);
	private static boolean LOAD_SO_SUCCESS = false;
	
	/**
	 * Construct a recipe instance in C++ side and return the recipe point. 
	 * Note: must explicitly delete the recipe by invoking {@link close}
	 * to avoid memory leak.
	 * @param recipePath
	 * @return
	 */
	public native ByteBuffer loadRecipe(String recipePath);
	
	/**
	 * Write the recipe object to file.
	 * @param handler
	 * @return
	 */

	public native boolean writeRecipe(ByteBuffer handler, String outPath);
	
	/**
	 * Delete the recipe resides in the memory.
	 * @param handler
	 * @return
	 */
	public native boolean closeRecipe(ByteBuffer handler);
	
	/**
	 * Update the recipe content.
	 * Different update operation and their parameters are encode in {@code xmlParameters}
	 * using XML format.
	 * 
	 * @param recipe the target recipe.
	 * @param xmlParameters
	 * @return the status of update.
	 */
	public native boolean updateRecipe(ByteBuffer recipe, String xmlParameters);
	
	/**
	 * Query information from Recipe, like plate type, IA number etc.
	 * Their parameters are encoded in  {@code xmlParameters}.
	 * The caller will decode the information from returned byte array. 
	 * @param recipe
	 * @param xmlParameters
	 * @return
	 */
	public native byte[] queryRecipe(ByteBuffer recipe, String xmlParameters);
	
	// Following is demo functions
	public native boolean jniUpdateSetName(ByteBuffer handler, String newSetName);
	
	public native byte[] jniGetPlateAlignmentImages(ByteBuffer handler);
	
	public native boolean jniUpdateSenseSlider(ByteBuffer handler, String xmlParameters);
	
		
	static {
		try{
			logger.info("loading native library from " + System.getProperty("java.library.path"));
			System.loadLibrary("RecipeWhole");
			LOAD_SO_SUCCESS = true;
			logger.info("load native library successfully.");
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			logger.fatal("Can't load native library");
		}
	}


	public static void main(String[] args) {
		JniDataOperations op = new JniDataOperations();
	}
}
