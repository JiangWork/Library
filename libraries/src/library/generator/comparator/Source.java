package library.generator.comparator;

/**
 * The source of comparison value.
 * @author Jiang
 * @date Jul 20, 2016 10:55:37 PM
 */
public interface Source {

	/**
	 * Get the value of this comparing source.
	 * @param target the object we need to compare.
	 * @return
	 */
	public Object getValue(Object target);
	
	/**
	 * Get the order of this source.
	 * Id two sources have the same priority,
	 * we use this order to determine the comparison 
	 * sequence.
	 * 
	 * @return
	 */
	public int getOrder();
}
