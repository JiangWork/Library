package library.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalPathClassloader extends ClassLoader {
	
	private String pathPrefix = null;
	private Pattern ABSOLUTE_PATH_PREFIX = Pattern.compile("^[ABCDEFabcdef]:/.*");
	
	public LocalPathClassloader(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}
	
	@Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("######loading " + name + " by " + this);
		String fileName = makeQualified(name);
		System.out.println("######qualified name " + fileName);
		try {
			InputStream is = new FileInputStream(fileName);
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
			throw new ClassNotFoundException("can't load class " + name, e);
		}
    }
	
	private String makeQualified(String name) {
		if(name.endsWith(".class")) {
			name = name.substring(0, name.length() - 6);
		}
		String qualifiedName = name.replace(".", "/");
		Matcher m = ABSOLUTE_PATH_PREFIX.matcher(name);
		if (!m.find()) {
			qualifiedName = pathPrefix + "/" + qualifiedName;
		} 
		qualifiedName += ".class";
		return qualifiedName;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        LocalPathClassloader classLoader = new LocalPathClassloader("D:/Eclipse Workspace/KT9x/bin/");
		Class<?> clazz = classLoader.loadClass("D:/Eclipse Workspace/KT9x/bin/library/processinvocation/example/Main.class");
		// will throw exception: attempted  duplicate class definition for name
		// same class will only loaded once for same classloader
		// Class<?> clazzAgain = classLoader.loadClass("D:\\Eclipse Workspace\\KT9x\\bin\\library\\processinvocation\\example\\Main.class");
		
		System.out.println("loaded by 1:" + clazz + " hash " + System.identityHashCode(clazz));
		System.out.println("loaded by 1:" +clazz.getClassLoader());
		
		
		LocalPathClassloader classLoader2 = new LocalPathClassloader("D:/Eclipse Workspace/KT9x/bin/");
		Class<?> clazz2 = classLoader2.loadClass("library/processinvocation/example/Main.class");
		System.out.println("loaded by 2:" +clazz2 + " hash " + System.identityHashCode(clazz2));
		System.out.println("loaded by 2:" +clazz2.getClassLoader());
		
		Method method = clazz.getDeclaredMethod("main", String[].class);
		method.invoke(null, new Object[]{new String[]{}});
		
		Method method2 = clazz2.getDeclaredMethod("main", String[].class);
		method2.invoke(null, new Object[]{new String[]{}});
	}

}
