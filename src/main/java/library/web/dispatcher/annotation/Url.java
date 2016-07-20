package library.web.dispatcher.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation determines the url can be handled by this Controller.
 * 
 * @author Miller
 * @date Mar 17, 2016 10:03:06 PM
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Url {

	String value();
}
