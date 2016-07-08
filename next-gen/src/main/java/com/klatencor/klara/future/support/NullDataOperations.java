package com.klatencor.klara.future.support;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * Null implementions of {@link JniDataOperations}
 * in case of native library load failure.
 * 
 * @author jiangzhao
 * @date  Jun 30, 2016
 * @version V1.0
 */
public class NullDataOperations extends JniDataOperations {

	private final static Logger logger = Logger.getLogger(NullDataOperations.class);
	private final static String ERROR_MSG = "Native library hasn't been load successfully, "
			+ "please check it. Default null implementations are used.";
	
	@Override
    public  ByteBuffer loadRecipe(String recipePath) {
		logger.info(ERROR_MSG);
		return null;
	}
	
	@Override
	public boolean writeRecipe(ByteBuffer handler, String outPath) {
		logger.info(ERROR_MSG);
		return true;
	}
	
	@Override
	public boolean closeRecipe(ByteBuffer handler) {
		logger.info(ERROR_MSG);
		return true;
	}
	
	@Override
	public boolean updateRecipe(ByteBuffer recipe, String xmlParameters) {
		logger.info(ERROR_MSG);
		return true;
	}
	
	@Override
	public byte[] queryRecipe(ByteBuffer recipe, String xmlParameters) {
		logger.info(ERROR_MSG);
		return new byte[0];
	}
	
//	// Following is demo functions
//	public boolean jniUpdateSetName(ByteBuffer handler, String newSetName) {
//		logger.info(ERROR_MSG);
//		return true;
//	}
//	
//	public byte[] jniGetPlateAlignmentImages(ByteBuffer handler) {
//		logger.info(ERROR_MSG);
//		return new byte[0];
//	}
//	
//	public boolean jniUpdateSenseSlider(ByteBuffer handler, String xmlParameters) {
//		logger.info(ERROR_MSG);
//		return true;
//	}
//	
	
}
