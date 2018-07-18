package demo.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
//import java.lang.ref.WeakReference;

public class ReferenceHolder<T> extends SoftReference<T> {

    private String key;
    
    public ReferenceHolder(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
        this.key = referent.getClass().getSimpleName() + ":" + System.identityHashCode(referent);
        // TODO Auto-generated constructor stub
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
}
