package org.smartframework.mvc.dispatcher.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark the methods should be automatically 
 * Initialized by framework
 * 
 * @author Miller
 * @date Mar 30, 2016 9:00:27 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Init {
	
	/**
	 * The parameters that can be passed to annotated method.
	 */
	String[] values() default {};
}
