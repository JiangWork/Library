package com.klatencor.klara.future.thrift.common;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Router {
	
	private final static Logger logger = Logger.getLogger(Router.class);
	private static Properties props;
	private static int DEFAULT_5XX_SUPPORT_VERSION = 349;
	private static int DEFAULT_6XX_FAB_SUPPORT_VERSION = 788;
	private static int DEFAULT_6XX_MS_SUPPORT_VERSION = 810;
	private static boolean enableRouting = false; 
	
	static {
		try {
			props = new Properties();
			FileInputStream inFile = new FileInputStream(
					"/klaL/logs/config.prop");
			props.load(inFile);
			inFile.close();
			enableRouting = "true".equalsIgnoreCase(props.getProperty("kt9xe.support.routing", "false"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Decide KT9Xe supports input version or not.
	 * To keep reliabilty, we only dispatch the latest version
	 * to xml-based KT9Xe.  
	 * 
	 * @param version the version needs to be decided.
	 * @return true if support, else false;
	 */
	public static boolean isSupport(int version) {
		if (!enableRouting) {
			return true;
		}
		int supportedVersion = 0;
		if (version <= 600) {   // 5xx
			String supportedVersionStr = props.getProperty("kt9xe.support.5xx.version", 
					String.valueOf(DEFAULT_5XX_SUPPORT_VERSION));
			supportedVersion = DEFAULT_5XX_SUPPORT_VERSION;
			try {
				supportedVersion = Integer.parseInt(supportedVersionStr);
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}
		
		} else if (version <= 800) { // 6xx FAB
			String supportedVersionStr = props.getProperty("kt9xe.support.6xx.fab.version", 
					String.valueOf(DEFAULT_6XX_FAB_SUPPORT_VERSION));
			supportedVersion = DEFAULT_6XX_FAB_SUPPORT_VERSION;
			try {
				supportedVersion = Integer.parseInt(supportedVersionStr);
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}			
			
		} else {  // 6xx MS
			String supportedVersionStr = props.getProperty("kt9xe.support.6xx.ms.version", 
					String.valueOf(DEFAULT_6XX_MS_SUPPORT_VERSION));
			supportedVersion = DEFAULT_6XX_MS_SUPPORT_VERSION;
			try {
				supportedVersion = Integer.parseInt(supportedVersionStr);
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}			
		}
		return supportedVersion > version;
	}
	
	/**
	 * Decide KT9Xe supports input file or not.
	 * To keep reliabilty, we only dispatch the latest version
	 * to xml-based KT9Xe.  
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isSupport(String filePath) {
		if (!enableRouting) {
			return true;
		}
		boolean isSupported = true;
		try {
			int version = getVersion(filePath);
			isSupported = isSupport(version);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return isSupported;
	}
	
	/**
	 * Get the version of recipe or inspection.
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static int getVersion(String filePath) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
		dis.readInt();
		int version = dis.readInt();
		dis.close();
		return version;
	}
 }
