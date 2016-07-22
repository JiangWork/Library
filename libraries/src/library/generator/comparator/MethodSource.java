package library.generator.comparator;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * Comparing value is from a method.
 * 
 * The method source must be the method without any parameter.
 * 
 * @author Jiang
 * @date Jul 20, 2016 10:58:11 PM
 */
public class MethodSource implements Source {
	private static final Logger logger = Logger.getLogger(MethodSource.class);
	
	private Method method;

	
	public MethodSource() {}
	
	public MethodSource(Method method) {
		this.method = method;
	}
	
	@Override
	public Object getValue(Object target) {
		// TODO Auto-generated method stub
		try {
			return method.invoke(target);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return method.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodSource) {
			return ((MethodSource)obj).method.equals(this.method);
		}
		return false;
	}

}
