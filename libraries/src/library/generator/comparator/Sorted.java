package library.generator.comparator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method or field is participated in sort.
 * @author Jiang
 * @date Jul 20, 2016 10:44:20 PM
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sorted {
	// the priority of sorting, default is highest priority.
	int priority() default 0;
	
	// the order of sorting, default is ASCENDING
	Order order() default Order.ASCENDING;
}
