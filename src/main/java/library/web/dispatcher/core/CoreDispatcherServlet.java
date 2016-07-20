package library.web.dispatcher.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import library.web.dispatcher.core.ControllerDescriptor.MethodInfo;
import library.web.dispatcher.core.ControllerDescriptor.ParameterInfo;

/**
 * Resolve the url and route it to the corresponding controller.
 * 
 * 
 * @author Miller
 * @date Mar 16, 2016 9:55:57 PM
 */
public class CoreDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CoreDispatcherServlet.class);
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		// locate the corresponding controller and invoke it
		String fullPath = request.getPathInfo();
		String ctrlUrl = fullPath.substring(0, fullPath.lastIndexOf('/'));
		String methodName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
		if (methodName.indexOf('.') != -1) {
			methodName = methodName.substring(0, methodName.indexOf('.'));
		}
		logger.debug(String.format("Request url:%s, controller path:%s, method name:%s", 
				fullPath, ctrlUrl, methodName));
		Object instance = getInstance(ctrlUrl);
		if (instance == null) {
			sendResponse(response, "No controller is found: " + ctrlUrl);
			return;
		}
		logger.debug("Controller is found: " + instance.getClass());
		ControllerDescriptor cd = getCtrlDescriptor(ctrlUrl);
		MethodInfo methodInfo = cd.getMethodInfo(methodName);
		Class<?> clazz = cd.getClazz();
		if (ParameterAware.class.isAssignableFrom(clazz)) {   // implement the interface ParameterAware
			Method method = cd.getMethod("setParameter");
			try {
				method.invoke(instance, request.getParameterMap());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (methodInfo == null) {
			sendResponse(response, "No method is found: " + methodName);
			return;
		}
		// bind the request parameter to method arguments 
		Object ret = null;
		try {
			if (0 == methodInfo.getArgsNum()) {  // 
				ret = methodInfo.getMethod().invoke(instance);
			} else {
				Object[] paramArray = new Object[methodInfo.getArgsNum()];
				Iterator<ParameterInfo> iter = cd.getParameterIter(methodName);
				int index = 0;
				while(iter.hasNext()) {
					ParameterInfo param = iter.next();
					String bindName = param.bindName;
					Class<?> tmpClazz = param.type;
					// here we only support non-array binding
					String value = request.getParameter(bindName);
					if (value == null) {
						sendResponse(response, "Binding error: binding name is not found: " + bindName);
						return;
					}
					try {
						paramArray[index++] = PrimitiveConverter.convert(value, tmpClazz);
					} catch(ConversionException e) {
						sendResponse(response, "Binding error:" + e.getMessage());
						logger.error(e.getMessage(), e);
						return;
					}
				}
				ret = methodInfo.getMethod().invoke(instance, paramArray);
			}
			sendResponse(response, PrimitiveConverter.stringify(ret));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		} 
	}
	
	public void sendResponse(HttpServletResponse response, String message) {
		try {
			DataOutputStream dos = new DataOutputStream(response.getOutputStream());
			dos.writeBytes(html(StringUtils.replace(message, "\n", "<br>")));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	 
	public String html(String content) {
		return String.format("<html> \n <head> <title>Dispatcher Returned Result</title> </head> \n <body>%s</body></html>", content);
	}
	
	public ControllerDescriptor getCtrlDescriptor(String url) {
		return ((Map<String, ControllerDescriptor>) super.getServletContext()
		.getAttribute(Constants.DESCRIPTOR_TABLE)).get(url);
	}
	
	// get the instance from cache by the url
	public Object getInstance(String url) {
		return ((Map<String, Object>) super.getServletContext()
			.getAttribute(Constants.INSTANCE_TABLE)).get(url);
	}
	
}
