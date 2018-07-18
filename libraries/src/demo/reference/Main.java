package demo.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        
        ReferenceQueue<int[]> referenceQueue = new ReferenceQueue<>();
        Map<ReferenceHolder<int[]>, String> cache = new HashMap<>();
        
        Thread queueChecker = new Thread(()-> {
            Reference ref;
            try {
                while((ref = referenceQueue.remove()) != null) {
                    ReferenceHolder<int[]> rref = (ReferenceHolder<int[]>) ref;
                    System.out.println("Cleaned: " + rref.getKey());
                    cache.remove(rref);
                    rref = null;
                    System.exit(-1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        );
        queueChecker.start();
        
        
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            ReferenceHolder<int[]> key = new ReferenceHolder<int[]>(new int[10], referenceQueue);
            cache.put(key, null);
//            Thread.sleep(10);
        }
        
    }

}
