package com.klatencor.klara.future.utils;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.object.FEConstants;
import com.klatencor.klara.future.server.ServerConfiguration;
import com.klatencor.klara.future.thrift.common.Router;

/**
 * IO related utilities.
 * 
 * @author jiangzhao
 * @date  Jun 21, 2016
 * @version V1.0
 */
public class IOUtils {
	
	private final static Logger logger = Logger.getLogger(IOUtils.class);

	public static String readString(DataInputStream dis) throws IOException  {
		String tempString = null;
		int byteLength = dis.readInt();
		byte[] byteArray = new byte[byteLength];
		for (int nl = 0; nl < byteLength; nl++) {
			byteArray[nl] = dis.readByte();
		}
		if (byteArray.length == 0) {
			tempString = new String("");
		} else {
			tempString = new String(byteArray);
		}
		return tempString;
	}
	
	/**
	 * Get the file type: recipe, inspection or else.
	 * @param filePath
	 * @return  0 for recipe, 1 for IR, else -1.
	 */
	public static int getFileType(String filePath) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(
					filePath));
			byte[] buf = new byte[4];
			dis.read(buf);
			dis.close();
			String type = new String(buf);
			if (type.equals("slrc"))
				return FEConstants.RCP_FILE;
			else if (type.equals("slir"))
				return FEConstants.IR_FILE;
			else
				return FEConstants.UNKNOW_FILE;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}
	
	/**
	 * Get the version of input file. 
	 * -1 is return if we can't get it.
	 * @param filePath
	 * @return version
	 */
	public static int getVersion(String filePath) {
		try {
			return Router.getVersion(filePath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}
	
	
	/**
	 * Write a String {@code content} to File {@code file}.
	 * No extra string is appended.
	 * @param file
	 * @param content
	 */
	public static void writeFile(File file, String content) {
		if (file.isDirectory()) {
			logger.error("input is a directory:" + file.getAbsolutePath());
			return;
		}
		File directory = file.getParentFile();
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				logger.error("can't make directory:" + directory.getAbsolutePath());
				return;
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			
		} catch (IOException e) {
			logger.error(e.getMessage(),e );
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					logger.error(e.getMessage(),e );
				}
			}
		}
	}

	/**
	 * Write a String {@code content} to File {@code file}.
	 * No extra string is appended.
	 * @param file
	 * @param content
	 */
	public static void writeFile(String filePath, String content) {
		File file = new File(filePath);
		writeFile(file, content);
	}
	
	/**
	 * Write a array of string to file, "\n" is appended at each line end.
	 * @param filePath
	 * @param lines a array of line string
	 */
	public static void writeFile(String filePath, String... lines) {
		StringBuilder sb = new StringBuilder();
		for (String line: lines) {
			sb.append(line);
			sb.append("\n");
		}
		writeFile(filePath, sb.toString());
	}
	
	/**
	 * Generate a random file name under directory {@link ServerConfiguration.TEMP_DIRECTORY}.
	 * <p>Note: no unique is ensured, no suffix is added.
	 * @param length the length of random part in file name.
	 * @return
	 */
	public static String tempFileName(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		char base = 'A';
		for (int i = 0; i < length; ++i) {
			int rand = random.nextInt(26);
			base = rand > 13 ? 'A':'a';
			sb.append((char)(base + random.nextInt(26)));
		}		
		return ServerConfiguration.TEMP_DIRECTORY + "/tmp_" + System.currentTimeMillis() + "_" + sb.toString();
	}

}
