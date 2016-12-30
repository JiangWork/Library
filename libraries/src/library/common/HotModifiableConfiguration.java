package library.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * A configuration utility to support any modification in source configuration  
 * file can take into effect immediately by checking the file date.
 * 
 * <p>This class is thread safe.
 *
 * @author jiangzhao
 * @date Dec 12, 2016
 * @version V1.0
 */
public class HotModifiableConfiguration {
	private final static Logger logger = Logger.getLogger(HotModifiableConfiguration.class);
	
	private File file;
	private volatile long fileLastModified = 0L;
	private ReentrantLock lock;  // for mutually exclusive loading 
	private Properties prop;
	
	public HotModifiableConfiguration(String filePath) {
		file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("File " + filePath + " doesn't exist.");
		}
		lock = new ReentrantLock();
		prop = new Properties();
	}	
	
	public String get(String key, String defaultVal) {
		String val = get(key);
		return val == null? defaultVal : val;
	}
	
	public String get(String key) {
		return getProp().getProperty(key);
	}
	
	/**
	 * Get value as Integer, if no such key or the value is not convertable, 
	 * NumberFormatException will be thrown.
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public int getInt(String key) {
		String value = get(key);
		return Integer.parseInt(value);
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}
	
	public boolean contains(String key) {
		return getProp().containsKey(key);
	}
		
	/**
	 * Get the properties and reload the file if necessary.
	 * 
	 * @return
	 */
	private Properties getProp() {
		if (file.exists()) {
			long currentMod = file.lastModified();
			long lastMod = fileLastModified;
			if (lastMod != currentMod) {
				logger.debug("Reloading the configuration: " + file.getAbsolutePath());
				prop = reload(lastMod);
				fileLastModified = currentMod;
			}
		}
		return prop;
	}
	
	/**
	 * Reload the configuration file.
	 * Thread-safe: only one thread can reload the configuration file.
	 * If other thread has already reloaded the configuration file, it will return.
	 * @param oldTimestamp the fileLastModified copy by the time call this method.
	 * @return the newly loaded properties.
	 */
	private Properties reload(long oldTimestamp) {
		lock.lock();
		try {
			if (fileLastModified != 0L && fileLastModified != oldTimestamp) { 
				logger.debug("Skip, already reloaded by another thread.");
				return prop;
			}
			Properties propLocal = new Properties();
			try {
				FileInputStream fis = new FileInputStream(file); 
				propLocal.load(fis);
				fis.close();
				return propLocal;
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return prop;
		} finally {
			lock.unlock();
		}
	}

}
