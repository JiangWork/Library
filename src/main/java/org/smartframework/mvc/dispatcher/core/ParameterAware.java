package org.smartframework.mvc.dispatcher.core;

import java.util.Map;

/**
 * Class implements this interface will be automatically 
 * set the parameter into the Object by the framework.
 * 
 * @author Miller
 * @date Mar 17, 2016 9:50:07 PM
 */
public interface ParameterAware {

	/**
	 * Set the parameters obtained from {@link HttpServletRequest} into
	 * the Object implements this interface.
	 * 
	 * @param parameter the map obtained from {@link HttpServletRequest}.
	 */
	public void setParameter(Map<String, String[]> parameter);
}
