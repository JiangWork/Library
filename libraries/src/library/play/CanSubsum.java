package library.play;

public class CanSubsum {

    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int N = 100;
        int[] eles = new int[] {2,4,6,1,5};
        boolean[] dp = new boolean[N];
        for (int i = 0; i < N; ++i)
            dp[i] = false;
        dp[0] = true;
        for (int i = 0; i < eles.length; ++i) {
            for (int k = N-1; k >= eles[i]; --k) {
                dp[k] |= dp[k-eles[i]];
            }
        }
        for (int i = 0; i < N; ++i)
            if (dp[i]) {
                System.out.println(i + ":" + dp[i]);
            }
    }

}
