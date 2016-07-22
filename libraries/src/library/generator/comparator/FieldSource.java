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
	
	public FieldSource() {}
	
	public FieldSource(Field field) {
		this.field = field;
		this.field.setAccessible(true);
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
	public String getName() {
		return field.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldSource) {
			return ((FieldSource)obj).field.equals(this.field);
		}
		return false;
	}

}
