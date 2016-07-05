package com.klatencor.klara.future.support;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * A wrapper for different recipe operations using JNI interfaces.
 * 
 * The classical pipeline to operate recipe is following:
 * <li><b>open</b>
 * <li><b>update or query</b>
 * <li><b>write</b>
 * <li><b>close</b>
 * 
 * @see JniDataOperations
 * @author jiangzhao
 * @date Jun 29, 2016
 * @version V1.0
 */
public class JniRecipeOperator implements Closeable {
	
	private final static Logger logger = Logger.getLogger(JniRecipeOperator.class);
	
	private ByteBuffer handler = null;
	private String recipePath;
	private JniDataOperations jni;

	public JniRecipeOperator() {
		jni = new JniDataOperations();
	}
	
	public JniRecipeOperator(String recipePath) {
		this();
		this.recipePath = recipePath;		
	}
	
	public boolean open() {
		if (handler == null) {
			File file = new File(recipePath);
			if (!file.exists()) {
				logger.error("Recipe doesn't exist: " + recipePath);
				return false;
			}
			handler = jni.loadRecipe(recipePath);
		} else {
			logger.debug("Recipe is already opened at path " + recipePath);
		}
		return handler != null;
	}
	
	public boolean write(String outPath) {
		if (handler != null) {
			return jni.writeRecipe(handler, outPath);
		} 
		logger.debug("Recipe hasn't been opened yet: " + recipePath);		
		return true;
	}
	
	public boolean renameRecipe(String newName) {
		if (handler != null) {
			return jni.jniUpdateSetName(handler, newName);
		}
		logger.debug("Recipe hasn't been opened yet: " + recipePath);	
		return true;
	}
	
	
	@Override
	public void close() throws IOException {
		if (handler != null) {
			jni.closeRecipe(handler);
			handler = null;
			logger.debug("Close recipe " + recipePath + " successfully.");
		}
	}

	public String getRecipePath() {
		return recipePath;
	}

	public void setRecipePath(String recipePath) {
		this.recipePath = recipePath;
	}
	
	

}
