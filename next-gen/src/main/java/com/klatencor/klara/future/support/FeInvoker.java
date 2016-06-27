package com.klatencor.klara.future.support;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.object.FEConstants;
import com.klatencor.klara.future.server.ServerConfiguration;
import com.klatencor.klara.future.utils.IOUtils;
import com.klatencor.klara.future.utils.Proc;
import com.klatencor.klara.future.utils.ProcResult;

/**
 * A unified interface to acess FE utilities, such as JNI, executables.
 * 
 * @author jiangzhao
 * @date  Jun 21, 2016
 * @version V1.0
 */
public class FeInvoker {

	private final static Logger logger = Logger.getLogger(FeInvoker.class);
	
	/**
	 * Generate XML file for all version recipe and IR.
	 * @param inPath
	 * @param outPath
	 * @return
	 */
	public static boolean generateXml(String inPath, String outPath) {
		int fileType = IOUtils.getFileType(inPath);
		int version = IOUtils.getVersion(inPath);
		switch(fileType) {
		case FEConstants.RCP_FILE: // recipe
			if (version > FEConstants.VERSION_BEGIN_MS) {
				return invokeFEXml("/export/home/kla90/felib/PrintRecipe",inPath, outPath);
			} else if (version > FEConstants.VERSION_BEGIN_FAB) {
				
			} else {
				
			}
			break;
		case FEConstants.IR_FILE: // IR
			break;
		default:
			logger.error("Unkonw file type: " + inPath);
			return false;
		}			
		return false;
	}
	
	public static boolean invokeFEXml(String executable, String inPath, String outPath) {
		Proc process = new Proc.ProcBuilder("FeXml", "/bin/bash", "-c", executable +" -xml2 -o " +  outPath + " " + inPath + " 1>/tmp/xml.log")
		.withTimeOut(ServerConfiguration.FE_INVOCATION_TIMEOUT).build();
		try {
			ProcResult result = process.run();
			if (result.getExitCode() != 0) {
				logger.error(result.getError());
				return false;
			} 
			return true;
		} catch (Exception e) {
			//ignored, already logged in process
		}
		return false;
	}
}
