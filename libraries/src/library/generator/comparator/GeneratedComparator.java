package library.generator.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratedComparator<T> implements Comparator<T> {

	private List<Criterion> criterions = new ArrayList<Criterion>();
	
	public GeneratedComparator() {}
	
	public List<Criterion> getCriterions() {
		return criterions;
	}
	public void setCriterions(List<Criterion> criterions) {
		this.criterions = criterions;
	}

	// here used to 
	private int compareInteral(Object one, Object another, Order order) {
		if(one instanceof Comparable) {
			if (order == Order.ASCENDING) {
				return ((Comparable<Object>)one).compareTo(another);
			} else {
				return -((Comparable<Object>)one).compareTo(another);
			}
		}
		throw new IllegalStateException("Oops, unsupported comparsion type: " + one.getClass().getName());

	}
	
	@Override
	public int compare(T o1, T o2) {
		// loop over criterion
		for (Criterion criterion: criterions) {
			Object val1 = criterion.getSource().getValue(o1);
			Object val2 = criterion.getSource().getValue(o2);
			int comparsion = compareInteral(val1, val2, criterion.getOrder());
			if (comparsion != 0) {
				return comparsion;
			}
		}
		return 0;
	}

}
