package com.klatencor.klara.future.support;

import java.nio.ByteBuffer;

/**
 * A interface to access FE shared library to get or change the
 * information of IR/recipe.
 * 
 * @author jiangzhao
 * @date  Jun 26, 2016
 * @version V1.0
 */
public class JniDataOperations {
	
	/**
	 * Invoke FE library to generate the XML of a Recipe or Inspection.
	 * 
	 * @param inPath the filePath of a recipe or inspection.
	 * @param outPath the output file path.
	 * @return true of success, otherwise false.
	 */
	private native boolean jniGenerateXml(String inPath, String outPath);
	
	/**
	 * Construct a recipe instance in C++ side and return a handler. 
	 * @param recipePath
	 * @return
	 */
	private native ByteBuffer jniLoadRecipe(String recipePath);
	
	private native ByteBuffer jniLoadInspection(String irPath);
	
	private native byte[] jniGetImageData(ByteBuffer handler, int defectIndex);
	
	
	private native boolean jniSetSetName(ByteBuffer handler, String newSetName);
	
	
	static {
		
		//System.setProperty("java.library.path", System.getProperty("java.library.path") + ":/home/jiazhao/felib");
		System.out.println( System.getProperty("java.library.path"));
		//System.load("/home/jiazhao/felib/libfe.so");
		System.loadLibrary("fe");
	}
	public boolean generateXml(String inPath, String outPath) {
		return jniGenerateXml(inPath, outPath);
	}

	public static void main(String[] args) {
		JniDataOperations op = new JniDataOperations();
		System.out.println(op.generateXml("in", "out"));
	}
}
