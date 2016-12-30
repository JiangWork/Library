package library.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {

	private List<String>  jarFilePaths;
	private List<JarFile> jarFiles;
	
	
	public JarClassLoader() {
		jarFiles = new ArrayList<JarFile>();
		jarFilePaths = new ArrayList<String>();
	}
	
	@Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("loading " + name + " by " + this);
		String qualifiedName = makeQualified(name);
		JarFile file = findJarFile(qualifiedName);
		if (file == null) {
			throw new ClassNotFoundException("Can't find class in jar file: " + qualifiedName);
		}
		JarEntry entry = file.getJarEntry(qualifiedName);
		InputStream is;
		try {
			is = file.getInputStream(entry);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int rc = 0;
			while((rc = is.read(buffer)) != -1) {
				bos.write(buffer, 0, rc);
			}
			is.close();
			byte[] clazzBytes = bos.toByteArray();
			Class<?> clazz = defineClass(null, clazzBytes, 0, clazzBytes.length);
			resolveClass(clazz);
			return clazz;
		} catch (IOException e) {
			throw new ClassNotFoundException("Error reading " + file.getName(), e);
		}
		
	}
	
	private JarFile findJarFile(String name) {
		loadJars(); // load jarFile if possible
		for (JarFile file:  jarFiles) {
			JarEntry entry = file.getJarEntry(name);
			if (entry != null) {
				return file;
			}
		}
		return null;		
	}
	
	private void loadJars() {
		for (String path: jarFilePaths) {
			File file = new File(path);
			if (file.exists()) {
				try {
					jarFiles.add(new JarFile(file));
				} catch (IOException ignored) {
					
				}
			} else {
				System.err.println("Skip " + path);
			}
		}
		jarFilePaths.clear();		
	}
	
	//  x.y.C.class -> x/y/C.class
	//  x.y.C -> x/y/C.class
	private String makeQualified(String name) {
		if(name.endsWith(".class")) {
			name = name.substring(0, name.length() - 6);
		}
		String qualifiedName = name.replace(".", "/");
		qualifiedName += ".class";
		return qualifiedName;
	}
	
	public synchronized void addJarPath(String path) {
		this.jarFilePaths.add(path);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		JarClassLoader loader = new JarClassLoader();
		for(String arg: args) {
			loader.addJarPath(arg);
		}
		Class<?> clazz = loader.loadClass("library.classloader.LocalPathClassloader");
		System.out.println("loaded class " + clazz + " by classloader " + clazz.getClassLoader());
		
		Method method = clazz.getDeclaredMethod("main", String[].class);
		method.invoke(null, new Object[]{new String[]{}});
	}	
}
