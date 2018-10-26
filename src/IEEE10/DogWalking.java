package IEEE10;

import java.io.*;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/9/26 11:21
 * @see IEEE10
 */
public class DogWalking {
    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    final static int INF = 1000000007;

    static int[] getBit(int n) {
        int bit[] = new int[17];
        int cur = 0;
        while (n > 0) {
            bit[cur++] = n % 2;
            n >>= 1;
        }
        return bit;
    }

    static void solve() throws IOException {
        int t = nextInt();

        while (t-- > 0) {
            int n = nextInt();
            int k = n - nextInt();
            int dogs[] = new int[n];
            for (int i = 0; i < n; i++) {
                dogs[i] = nextInt();
            }

            Arrays.sort(dogs);
            Queue<Integer> queue = new PriorityQueue<>();
            for (int i = 0; i < n - 1; i++) {
                queue.offer(dogs[i + 1] - dogs[i]);
            }
            int ans = 0;
            while (k-- > 0) {
                ans += queue.poll();
            }
            pw.print(ans);
            if (t > 0) {
                pw.println();
            }
        }

    }

    public static void main(String args[]) throws IOException {
//        boolean oj = System.getProperty("ONLINE_JUDGE") != null;
//        if (!oj) {
//            System.setIn(new FileInputStream("src/in.txt"));
//        }
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StreamTokenizer(br);
        pw = new PrintWriter(new OutputStreamWriter(System.out));
        st.ordinaryChar('\'');
        st.ordinaryChar('\"');
        st.ordinaryChar('/');

        long t = System.currentTimeMillis();
        solve();
//        if (!oj) {
//            pw.println("[" + (System.currentTimeMillis() - t) + "ms]");
//        }
        pw.flush();
    }


    private static int nextInt() throws IOException {
        st.nextToken();
        return (int) st.nval;
    }

    private static long nextLong() throws IOException {
        st.nextToken();
        return Long.parseLong(st.sval);
    }

    private static double nextDouble() throws IOException {
        st.nextToken();
        return st.nval;
    }

    private static String[] nextSS(String reg) throws IOException {
        return br.readLine().split(reg);
    }

    private static String nextLine() throws IOException {
        return br.readLine();
    }
}
