package demo.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SingletonViaReflect {

	private SingletonViaReflect(){};
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Constructor<?> constructor = SingletonViaReflect.class.getDeclaredConstructor();
		SingletonViaReflect singleton = (SingletonViaReflect) constructor.newInstance();
		System.out.println("Hashcode for " + singleton.getClass() + " is:" + System.identityHashCode(singleton));
	}

}
