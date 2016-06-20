package com.klatencor.klara.future.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 
 * A convenient utility to operate with file.
 *
 * @author jiangzhao
 * @date Jun 20, 2016
 * @version V1.0
 */
public class FileUtils {
	
	private final static Logger logger = Logger.getLogger(FileUtils.class);
	
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
			if (directory.mkdirs()) {
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
	
	

}
