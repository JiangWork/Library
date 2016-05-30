package org.smartframework.mvc.dispatcher.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.smartframework.mvc.dispatcher.annotation.Init;
import org.smartframework.mvc.dispatcher.annotation.Param;
import org.smartframework.mvc.dispatcher.annotation.Url;

/**
 * Store the meta information for a controller.
 * 
 * @author Miller
 * @date Mar 17, 2016 11:56:24 PM
 */
public class ControllerDescriptor {

	private Map<String, MethodInfo> methodMap;
	private Class<?> clazz;
	// a url starts with "/", like /first/action
	private String requestUrl;
	
	public ControllerDescriptor(Class<?> clazz) {
		methodMap = new HashMap<String, MethodInfo>();
		this.clazz = clazz;
	}
	
	public int getArgsNum(String method) {
		if (methodMap.containsKey(method)) {
			MethodInfo m = methodMap.get(method);
			return m.params.size();
		}
		return 0;
	}
	
	public Iterator<ParameterInfo> getParameterIter(String method) {
		if (methodMap.containsKey(method)) {
			MethodInfo m = methodMap.get(method);
			return m.params.iterator();
		}
		return new Iterator<ParameterInfo>() {
			@Override
			public boolean hasNext() {
				return false;
			}
			@Override
			public ParameterInfo next() {
				return null;
			}
		};
	}
	
	public MethodInfo getMethodInfo(String method) {
		return methodMap.get(method);
	}
	
	public Method getMethod(String method) {
		return methodMap.get(method).method;
	}
	
	/**
	 * Get the first method is annotated by {@link Init}.
	 * @return the first annotated method, null if not exists.
	 */
	public Method getInitMethod() {
		Iterator<MethodInfo> methods = methodMap.values().iterator();
		while(methods.hasNext()) {
			MethodInfo mi = methods.next();
			Init anno = mi.method.getAnnotation(Init.class);
			if (anno != null) {
				return mi.method;
			}
		}
		return null;
	}
	
	public String getBindName(String method, int paraIndex) {
		if (methodMap.containsKey(method)) {
			MethodInfo m = methodMap.get(method);
			if (paraIndex < m.getParams().size()) {
				return m.getParams().get(paraIndex).bindName;
			}
		}
		return null;
	}
	
	public static ControllerDescriptor describe(Class<?> clazz) {
		ControllerDescriptor cd = new ControllerDescriptor(clazz);
		cd.parseRequestUrl();
		Method[] methods = clazz.getMethods();
		for (Method method: methods) {
			int modifier = method.getModifiers();
			if ((modifier & Modifier.ABSTRACT) != 0
					|| (modifier & Modifier.NATIVE) != 0
					|| (modifier & Modifier.PUBLIC) == 0)
			{
				continue;
			}
			method.setAccessible(true);
			MethodInfo mi = new MethodInfo();
			mi.setMethod(method);
			mi.setReturnType(method.getReturnType());
			Class<?>[] paraClazz = method.getParameterTypes();
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			int index = 0;
			for (Annotation[] parameterAnnotation: parameterAnnotations) {
				String bindName = null;
				for (int i = 0; i < parameterAnnotation.length; ++ i) {
					if (parameterAnnotation[i] instanceof Param) {
						bindName = ((Param)parameterAnnotation[i]).value();
					}
				}
				mi.addParam(new ParameterInfo(bindName, paraClazz[index++]));
			}
			cd.methodMap.put(method.getName(), mi);
		} 
		return cd;
	}
	
	private void parseRequestUrl() {
		String url = null;
		if(clazz.isAnnotationPresent(Url.class)) {
			Url anno = clazz.getAnnotation(Url.class);
			url = anno.value();
			if (!url.startsWith("/")) {
				url = "/" + url; 
			}
		} else {
			url = NamingConventionUtils.toURL(clazz.getSimpleName());
		}
		this.requestUrl = url;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public static class MethodInfo {
		private List<ParameterInfo> params;
		private Class<?> returnType;
		private Method method;
		
		public MethodInfo() {
			params = new ArrayList<ParameterInfo>();
		}
		public void addParam(ParameterInfo param) {
			this.params.add(param);
		}
		public List<ParameterInfo> getParams() {
			return params;
		}
		public void setParams(List<ParameterInfo> params) {
			this.params = params;
		}
		public Class<?> getReturnType() {
			return returnType;
		}
		public void setReturnType(Class<?> returnType) {
			this.returnType = returnType;
		}
		public String getMethodName() {
			return method.getName();
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		
		public int getArgsNum() {
			return params.size();
		}
		
		public String getBindName(int index) {
			return params.get(index).bindName;
		}
		
		public Class<?> getClazz(int index) {
			return params.get(index).type;
		}
		
	}
	
	public static class ParameterInfo {
		public String bindName;
		public Class<?> type;
		public ParameterInfo(String bindName, Class<?> type) {
			this.bindName = bindName;
			this.type = type;
		}
	}
	
	public String toString() {
		return "Class:" + clazz.getName();
	}
}
