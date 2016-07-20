package library.generator.comparator;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

/**
 * Comparing value is from a field.
 * 
 * @author Jiang
 * @date Jul 20, 2016 11:12:39 PM
 */
public class FieldSource implements Source {

	private static final Logger logger = Logger.getLogger(FieldSource.class);
	
	private Field field;
	private int order;
	
	public FieldSource(int order) {
		this.order = order;
	}
	
	public FieldSource(Field field, int order) {
		this.field = field;
		this.field.setAccessible(true);
		this.order = order;
	}
	
	@Override
	public Object getValue(Object target) {
		// TODO Auto-generated method stub
		try {
			return field.get(target);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int getOrder() {
		return order;
	}
	
	

}
