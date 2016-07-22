package library.common;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Find the desired classes as {@link Class} under current classpath.
 * 
 * @author Miller
 * @date Mar 17, 2016 12:33:41 AM
 */
public class ClassUtils {

	private static final Logger logger = Logger.getLogger(ClassUtils.class);
	private static final String pathSeparator = File.separator;
	
	
	/**
	 * Find all classes under specific package.
	 * If packageName is null, empty or '/', then classes under root package will be returned.
	 * 
	 * @param packageName the package needs to be searched.
	 * @return a set of Class.
	 * @throws IOException 
	 */
	public static Set<Class<?>> findAllClasses(String packageName) throws IOException {
		return findClasses(packageName, new ClassChecker() {
			public boolean check(Class<?> clazz) {
				return true;
			}
			
		});
	}
	
	/**
	 * Find all classes whose names are end with nameSuffix under specific package.
	 * If nameSuffix is empty, then all classes will be returned.
	 * 
	 * @param packageName the package needs to be searched.
	 * @param nameSuffix  the class name should be end with nameSuffix.
	 * @return a set of desired Class.
	 * @throws IOException 
	 */
	public static Set<Class<?>> findClassesNameEnd(String packageName, String nameSuffix) throws IOException {
		final String suffix = StringUtils.strip(nameSuffix);
		return findClasses(packageName, new ClassChecker() {
			public boolean check(Class<?> clazz) {
				if (suffix.length() != 0 && !clazz.getSimpleName().endsWith(suffix)) {
					return false;
				}
				return true;
			}
		});
	}
	
	/**
	 * Find all the classes under specific package which extends the class <code>parentClazz</code>.
	 * 
	 * @param packageName the package needs to be searched.
	 * @param parentClazz the parent class.
	 * @return a set of desired Class.
	 * @throws IOException
	 */
	public static Set<Class<?>> findSubclasses(String packageName, final Class<?> parentClazz) throws IOException {
		return findClasses(packageName, new ClassChecker(){
			public boolean check(Class<?> clazz) {
				return parentClazz.isAssignableFrom(clazz);
			}
		});
	}
	
	/**
	 * Find all classes who are annotated by {@code annClazz} under specific package.
	 * If {@code annClazz} is null, then all classes will be returned.
	 * 
	 * @param packageName the package needs to be searched.
	 * @param annClazz the annotation should be checked.
	 * @return a set of desired Class.
	 * @throws IOException 
	 */
	public static Set<Class<?>> findClassesWithAnnotation(String packageName, Class<? extends Annotation> annClazz) throws IOException {
		final Class<? extends Annotation> annoClass = annClazz; 
		return findClasses(packageName, new ClassChecker() {
			public boolean check(Class<?> clazz) {
				if (annoClass == null) {
					return true;
				}
				Annotation[] annos = clazz.getAnnotations();
				for (Annotation anno: annos) {
					if(annoClass.isAssignableFrom(anno.getClass())) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	
	/**
	 * Find the desired classes under specific package in java classpath.
	 * 
	 * @param packageName the package needs to be searched.
	 * @param checker a checker determine whether current Class should be added.
	 * @return a set of Class.
	 * @throws IOException 
	 */
	private static Set<Class<?>> findClasses(String packageName, ClassChecker checker) throws IOException {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		String path = formPath(packageName);
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url != null) {
				if ("file".equals(url.getProtocol())) {
					String diskPath = url.getPath();
					File directory = new File(diskPath); 
					if (directory.isDirectory()) {  // search the file names end with .class
						File[] files = directory.listFiles();
						for (File file: files) {
							if (!file.isFile() || !file.getName().endsWith(".class"))  continue;
							try {
								Class<?> clazz = ClassUtils.class.getClassLoader()
										.loadClass(packageName+ "." + StringUtils.removeEnd(file.getName(), ".class"));
								if (checker.check(clazz)) {
									classSet.add(clazz);
								}
							} catch (ClassNotFoundException e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				} else if ("jar".equals(url.getProtocol())) {
					String diskPath = url.getPath()
							.substring("file:".length(), url.getPath().lastIndexOf('!'));
					File file = new File(diskPath);
					if (file.exists()) {
						JarFile jarFile = new JarFile(file);
						Enumeration<JarEntry> enums = jarFile.entries();
					    while(enums.hasMoreElements()) {
					    	JarEntry entry = enums.nextElement();
					    	if (entry.getSize() == 0)	continue;
					    	String name = entry.getName();
					    	if (name.endsWith(".class") && name.lastIndexOf('/') == path.length()) {
					    		try {
									Class<?> clazz = ClassUtils.class.getClassLoader()
											.loadClass(StringUtils.removeEnd(name, ".class").replace('/', '.'));
									if (checker.check(clazz)) {
										classSet.add(clazz);
									}
								} catch (ClassNotFoundException e) {
									logger.error(e.getMessage(), e);
								}
					    	}
					    }
					    jarFile.close();
					} else {
						logger.error("cannot find file:" + diskPath);
					}
				}
			}
		}
		
		return classSet;
	}
	
	/**
	 * Find the package names under specific package.
	 * If parentPackage is empty or null or "/", then all package names will be returned.
	 * @param parentPackage
	 * @return a set of package names
	 * @throws IOException 
	 */
	public static Set<String> findPackages(String parentPackage) throws IOException {		
		Set<String> packageSet = new HashSet<String>();
		String path = formPath(parentPackage);
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url != null) {
				if ("file".equals(url.getProtocol())) {
					String diskPath = url.getPath();
					findPackage(StringUtils.removeEnd(diskPath, path), new File(diskPath), packageSet);
				} else if ("jar".equals(url.getProtocol())) {
					String diskPath = url.getPath()
							.substring("file:".length(), url.getPath().lastIndexOf('!'));
					File file = new File(diskPath);
					if (file.exists()) {
						JarFile jarFile = new JarFile(file);
						Enumeration<JarEntry> enums = jarFile.entries();
					    while(enums.hasMoreElements()) {
					    	JarEntry entry = enums.nextElement();
					    	if (entry.getSize() == 0)	continue;
					    	String name = entry.getName();
					    	if (name.startsWith(path) && name.endsWith(".class")) {
					    		packageSet.add(name.substring(0, name.lastIndexOf('/'))
					    				.replace('/', '.'));
					    	}
					    }
					    jarFile.close();
					} else {
						logger.error("cannot find file:" + diskPath);
					}
				
				}
				
			}
		}
		
	    packageSet.remove(parentPackage);
		return packageSet;
	}
	
	
	private static void findPackage(String prefix, File file, Set<String> resultSet) {
		if (file == null) {
			return;
		}
		if (file.isFile()) {
			if (file.getName().endsWith(".class")) {
				String path = file.getParentFile().getAbsolutePath();
				String packagePath = StringUtils.removeStart(path, prefix);
				String packageName = StringUtils.replace(packagePath, pathSeparator, ".");
				if (packageName.length() == 0) {
					packageName = "/";
				}
				resultSet.add(packageName);
			}
			return;
		}
		if (file.isDirectory()) {
			File[] dirs = file.listFiles();
			for (File dir: dirs) {
				findPackage(prefix, dir, resultSet);
			}
		}
	}

	private static String formPath(String packageName) {
		if (packageName == null || StringUtils.isEmpty(packageName)) {
			packageName = "";
		}
		if (packageName.startsWith("/")) {
			packageName = packageName.substring(1);
		}
		String path = StringUtils.replace(packageName, ".", "/");
		return path;
	}
	
	/**
	 * The interface to check whether the specific {@link Class} is qualified.
	 * 
	 * @author Miller
	 * @date Mar 16, 2016 10:14:52 PM
	 */
	public static interface ClassChecker  {
		
		/**
		 * Check the validity of input Class.
		 * @param clazz
		 * @return 
		 */
		public boolean check(Class<?> clazz);
	}
}
