package library.generator.comparator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class ComparatorGenerator<T>{
	
	private static final Logger logger = Logger.getLogger(ComparatorGenerator.class);
	
	private Set<Criterion> criterions = new HashSet<Criterion>();
	private Map<String, CompareConfig> configMap = new HashMap<String, CompareConfig>();
	
	public ComparatorGenerator<T> add(String name, int priority, Order order) {
		configMap.put(name, new CompareConfig(name, priority, order));
		return this;
	}
	
	private void collectCriterions(Class<T> clazz) {
		// Field first
		Field[] fields = clazz.getDeclaredFields();
		for (Field field: fields) {
			Criterion criterion = getFieldCriterion(field);
			if (criterion != null) {
				if(!criterions.add(criterion)) {
					logger.debug("Can't add criterion: " + criterion.toString());
				}
			}
		}
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method: methods) {
			Criterion criterion = getMethodCriterion(method);
			if (criterion != null) {
				if(!criterions.add(criterion)) {
					logger.debug("Can't add criterion: " + criterion.toString());
				}
			}
		}
	}
	
	private Criterion getFieldCriterion(Field field) {
		Criterion criterion = null;
		String name = field.getName();
		if (configMap.containsKey(name)) {			
			CompareConfig config = configMap.get(name);
			criterion = new Criterion();
			criterion.setPriority(config.priority);
			criterion.setOrder(config.order);
			criterion.setSource(new FieldSource(field));
			Sorted sorted = field.getAnnotation(Sorted.class);
			if (sorted != null) {
				logger.debug("Ignored annotation setting: " + sorted);
			}
		} else {  // check annotation
			Sorted sorted = field.getAnnotation(Sorted.class);
			if (sorted != null) {
				criterion = new Criterion();
				criterion.setSource(new FieldSource(field));
				criterion.setOrder(sorted.order());
				criterion.setPriority(sorted.priority());
			}
		}
		return criterion;
	}
	
	private Criterion getMethodCriterion(Method method) {
		Criterion criterion = null;
		String name = method.getName();
		if (configMap.containsKey(name)) {			
			CompareConfig config = configMap.get(name);
			criterion = new Criterion();
			criterion.setPriority(config.priority);
			criterion.setOrder(config.order);
			criterion.setSource(new MethodSource(method));
			Sorted sorted = method.getAnnotation(Sorted.class);
			if (sorted != null) {
				logger.debug("Ignored annotation setting: " + sorted);
			}
		} else {  // check annotation
			Sorted sorted = method.getAnnotation(Sorted.class);
			if (sorted != null) {
				criterion = new Criterion();
				criterion.setSource(new MethodSource(method));
				criterion.setOrder(sorted.order());
				criterion.setPriority(sorted.priority());
			}
		}
		return criterion;
	}
	
	public GeneratedComparator<T> generate(Class<T> clazz) {
		GeneratedComparator<T> comparator = new GeneratedComparator<T>();
		collectCriterions(clazz);
		List<Criterion> clist = new ArrayList<Criterion>();
		Iterator<Criterion> iter = criterions.iterator();
		while(iter.hasNext()) {
			clist.add(iter.next());
		}
		Collections.sort(clist);
		if (logger.isDebugEnabled()) {
			for (Criterion c: clist) {
				logger.debug(c.toString());
			}
		}
		comparator.setCriterions(clist);
		return comparator;		
	}
	
	private class CompareConfig {
		private String name;
		private int priority;
		private Order order;
		public CompareConfig(String name, int priority, Order order) {
			this.name = name;
			this.priority = priority;
			this.order = order;
		}
	}
}
