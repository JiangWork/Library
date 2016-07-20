package library.generator.comparator;

/**
 * A sort criterion defined by the user.
 * 
 * If two criterions have the same priority, {@link Source}'s 
 * order is used to determine the comparison order.
 * 
 * 
 * @author Jiang
 * @date Jul 20, 2016 11:22:48 PM
 */
public class Criterion implements Comparable<Criterion>{

	private Source source;
	private int priority;
	private Order oder;
	
	
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Order getOder() {
		return oder;
	}
	public void setOder(Order oder) {
		this.oder = oder;
	}
	
	@Override
	public int compareTo(Criterion o) {
		if (o.priority == this.priority) {
			return o.source.getOrder() - this.source.getOrder();
		}
		return o.priority - this.priority;
	}
	
	
}
