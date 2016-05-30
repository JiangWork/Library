package org.smartframework.mvc.dispatcher.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.smartframework.common.utils.ClassUtils;
import org.smartframework.mvc.dispatcher.annotation.Init;
import org.smartframework.mvc.dispatcher.annotation.Url;


/**
 * This is a listener on the startup/destroy of application.
 * 
 * We will initialize the URL mapping tables here through scanning 
 * the package configured in web.xml. The parameter key is: 
 * 
 * @author Miller
 * @date Mar 17, 2016 10:11:13 PM
 */
public class ContainerListener implements ServletContextListener {
	
    private static final Logger logger = Logger.getLogger(ContainerListener.class);
	
    // the url to instance mapping table
    private final Map<String, Object> instanceTable = new HashMap<String, Object>();
    // the url to ControllerDescriptor mapping table
    private final Map<String, ControllerDescriptor> descriptorTable 
    						= new HashMap<String, ControllerDescriptor>();
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String pkgName = sce.getServletContext().getInitParameter(Constants.SCANNED_PACKAGE);
		logger.info("Scanning package: " + pkgName);
		if (pkgName != null) {
			try {
				Set<Class<?>> nameQualifiedclasses = ClassUtils.findClassesNameEnd(pkgName, Constants.CONTROLLER_SUFFIX);
				Set<Class<?>> annoQualifiedClasses = ClassUtils.findClassesWithAnnotation(pkgName, Url.class);
				initializeTables(nameQualifiedclasses);
				initializeTables(annoQualifiedClasses);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} 
		}
		sce.getServletContext().setAttribute(Constants.INSTANCE_TABLE, instanceTable);
		sce.getServletContext().setAttribute(Constants.DESCRIPTOR_TABLE, descriptorTable);
		logger.info("Initialized instanceTable: " + descriptorTable.toString());
	}

	private void initializeTables(Set<Class<?>> classes) throws InstantiationException, IllegalAccessException {
		Iterator<Class<?>>  iter = classes.iterator();
	    while(iter.hasNext()) {
	    	Class<?> clazz = iter.next();
	    	int modifier = clazz.getModifiers();
	    	if ((modifier&Modifier.ABSTRACT) != 0)	continue;
	    	ControllerDescriptor cd = ControllerDescriptor.describe(clazz);
	    	String requestUrl = cd.getRequestUrl();
	    	Object instance = clazz.newInstance();
	    	Method method = cd.getInitMethod();
	    	if (method != null) {
	    		Init anno = method.getAnnotation(Init.class);
	    		String[] args = anno.values();
	    		try {
	    			method.invoke(instance, (Object[])args);
	    			logger.info("Invoke inital method " + method.getName());
	    		} catch (IllegalArgumentException | InvocationTargetException e) {
	    			logger.error(e.getMessage(), e);
	    		}
	    	}
	    	instanceTable.put(requestUrl, instance);
	    	descriptorTable.put(requestUrl, cd);
	    }
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		instanceTable.clear();
		descriptorTable.clear();
	}

}
