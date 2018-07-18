package demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StoreSimpleProxy implements InvocationHandler {

    private Object target;

    private StoreSimpleProxy(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
	public static <T> T bind(Object target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new StoreSimpleProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	System.out.println(this + ": interceptoring ");
        return method.invoke(target, args);
    }
}