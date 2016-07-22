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
	private Order order;
	
	
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@Override
	public int compareTo(Criterion o) {
		if (o.priority == this.priority) {
			return source.getName().compareTo(o.source.getName());
		}
		return this.priority - o.priority;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Criterion) {
			return ((Criterion)obj).source.equals(this.source);
		}
		return false;
	}
	
	public String toString() {
		return String.format("source=%s priority=%d order=%s", 
				source.getName(), priority, order);
	}
}
