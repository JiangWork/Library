package library.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Proxy {

	
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Object target, Actions action) {
		return (T) java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), new Invocation(target, action));
	}
	

	public static <T> T getProxy(Object target) {
		return getProxy(target, new Actions() {

			@Override
			public void before() {
				System.out.println("Hello, before.");
			}

			@Override
			public void after() {
				System.out.println("Hello, after.");
			}
			
		});
	}
	
	public interface Actions {
		public void before();
		public void after();
	}
	
	
	public static class Invocation implements InvocationHandler {

		private Object target;
		private Actions action;
		
		public Invocation(Object target, Actions action) {
			this.target = target;
			this.action = action;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			action.before();
			Object result = method.invoke(target, args);
			action.after();
			return result;
		}
		
	}
} 
