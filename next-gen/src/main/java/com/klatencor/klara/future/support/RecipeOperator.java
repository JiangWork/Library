package com.klatencor.klara.future.support;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.klatencor.klara.future.support.object.Image;
import com.klatencor.klara.future.thrift.common.TSenseSlider;
import com.klatencor.klara.future.utils.IOUtils;
import com.klatencor.klara.future.utils.XmlUtil;

/**
 * A wrapper for different recipe operations using JNI interfaces.
 * 
 * The classical pipeline to operate recipe is following:
 * <li><b>open</b> open the recipe.
 * <li><b>update or query</b> do the update/query operations.
 * <li><b>write</b>write the recipe to disk file.
 * <li><b>close</b>close the recipe.
 * 
 * @see JniDataOperations
 * @author jiangzhao
 * @date Jun 29, 2016
 * @version V1.0
 */
public class RecipeOperator implements Closeable {
	
	private final static Logger logger = Logger.getLogger(RecipeOperator.class);
	
	private ByteBuffer handler = null;
	private String recipePath;
	private JniDataOperations jni;

	public RecipeOperator() {
		jni = new JniDataOperations();
		if (!JniDataOperations.LOAD_SO_SUCCESS) {
			logger.info("NullDataOperations is used instead.");
			jni = new NullDataOperations();
		}		
	}
	
	public RecipeOperator(String recipePath) {
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
			XmlUtil xml = new XmlUtil();
			Element root = xml.newDocument("Jni");
			root.appendChild(xml.createElement("ID", "20002"));
			root.appendChild(xml.createElement("RcpName", newName));
			String xmlParameters;
			try {
				xmlParameters = xml.getString();
				logger.debug("xmlParameters:" + xmlParameters);
				return jni.updateRecipe(handler, xmlParameters);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}			
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
	
	/////////////////////////////////////////////////////////////////////////////////
	//////////////////////////  Following is update methods /////////////////////////
	/**
	 * Update the sense slider by given a list of sense sliders.
	 * @param senseSliders
	 * @return
	 */
	public boolean updateSenseSlider() {		
		
		return true;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	//////////////////////////  Following is query methods /////////////////////////
	
	/**
	 * Get the plate alignment images from Recipe.
	 * Empty list is returned if no images.
	 * @param needImageData if image data is needed.
	 * @return a list of image.
	 */
	public List<Image> getPlateAlignmentImages(boolean needImageData) {
		XmlUtil xml = new XmlUtil();
		Element root = xml.newDocument("Jni");
		root.appendChild(xml.createElement("ID", "10001"));
		root.appendChild(xml.createElement("version", "1"));
		List<Image> imageList = new ArrayList<Image>();
		try {
			String xmlParameters = xml.getString();
			logger.debug("xmlParameters:" + xmlParameters);
			byte[] buffer = jni.queryRecipe(handler, xmlParameters);
			//System.out.print("buffer size:" + buffer.length + " content:" + StringUtils.toHex(buffer));
			imageList  = ImagesBytesConverter.INSTANCE.to(buffer, 0);
			buffer = null; 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return imageList;
	}
	
	public Image getPlateOverviewImage() {
		return null;
	}
	
	public List<TSenseSlider> getIaSenseSliders(int iaIndex) {
		XmlUtil xml = new XmlUtil();
		Element root = xml.newDocument("Jni");
		root.appendChild(xml.createElement("ID", "10003"));
		root.appendChild(xml.createElement("IA", null, "index", String.valueOf(iaIndex)));
		List<TSenseSlider> list = new ArrayList<TSenseSlider>();
		try {
			String xmlParameters = xml.getString();
			logger.debug("xmlParameters:" + xmlParameters);
			byte[] buffer = jni.queryRecipe(handler, xmlParameters);
			list = SenseSlidersBytesConverter.INSTANCE.to(new BytesWapper(buffer));
			buffer = null; 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;		
	}
	
	public TSenseSlider getCertainSenseSlider(int iaIndex, int knobIndex) {
		XmlUtil xml = new XmlUtil();
		Element root = xml.newDocument("Jni");
		root.appendChild(xml.createElement("ID", "10003"));
		root.appendChild(xml.createElement("IA", null, "index", String.valueOf(iaIndex)));
		root.appendChild(xml.createElement("knob", null, "index", String.valueOf(knobIndex)));
		TSenseSlider ss = new TSenseSlider();
		try {
			String xmlParameters = xml.getString();
			logger.debug("xmlParameters:" + xmlParameters);
			byte[] buffer = jni.queryRecipe(handler, xmlParameters);
			ss = SenseSliderBytesConverter.INSTANCE.to(new BytesWapper(buffer));
			buffer = null; 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ss;
	}
	
	public int geInspectionAreaCount() {
		return 0;
	}
	
	public String getRecipePath() {
		return recipePath;
	}

	public void setRecipePath(String recipePath) {
		this.recipePath = recipePath;
	}	
	
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.err.println("Usage: recipePath new-recipe-name outputDirectory");
			return;
		}
		String recipePath = args[0];
		String newName = args[1];
		String output = args[2];
		
		RecipeOperator op = new RecipeOperator(recipePath);
		
		System.out.println(">>>Open status: " + op.open());
		System.out.println(">>>Rename status:" + op.renameRecipe(newName));
		System.out.println(">>>Write status:" + op.write(output+"/"+ newName +".rcp"));
		System.out.println(">>>Rename Done.");
		List<Image> images = op.getPlateAlignmentImages(true);
		System.out.println(">>> imagesize: "  + images.size());
		op.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(output + "/paImages/images.txt")));
		// output Images
		for(int i = 0; i < images.size(); ++i) {
			Image image = images.get(i);
			bw.write(image.toString() + "\n");
			IOUtils.writeImages(image.getImageData(), image.getWidth(), image.getHeight(), output + "/paImages/" + i + ".gif", "gif");
		}
		bw.close();
				
	}

}
