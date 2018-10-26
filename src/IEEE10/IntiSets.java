package IEEE10;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class IntiSets {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    static void getDiv(Map<Long, Integer> map, long n) {
        long sqrt = (long) Math.sqrt(n);
        for (long i = sqrt; i >= 2; i--) {
            if (n % i == 0) {
                getDiv(map, i);
                getDiv(map, n / i);
                return;
            }
        }
        map.put(n, map.getOrDefault(n, 0) + 1);
    }


    public static boolean[] generatePrime(int n) {
        boolean p[] = new boolean[n + 1];
        p[2] = true;

        for (int i = 3; i <= n; i += 2) {
            p[i] = true;
        }

        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (!p[i]) {
                continue;
            }
            for (int j = i * i; i <= n; j += i << 1) {
                p[j] = false;
            }
        }
        return p;
    }

    static int pow(long a, int n) {
        long ans = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans = (ans * a) % INF;
            }
            a = (a * a) % INF;
            n >>= 1;
        }
        return (int) ans;
    }

    static long sum(long low, long high, long div) {
        long len = high - low + 1;
        long s = high + low;
        if ((len & 1) == 0) {
            len >>= 1;
        } else {
            s >>= 1;
        }
        len %= INF;
        s %= INF;
        return ((len * s) % INF) * div % INF;
    }

    private static void solve() throws IOException {
        int q = nextInt();
        while (q-- > 0) {
            long n = nextLong();
            long a = nextLong();
            long b = nextLong();

            long sum = sum(a, b, 1);

            Map<Long, Integer> map = new HashMap<>();
            getDiv(map, n);
            long fac[] = new long[map.size()];
            int idx = 0;
            for (long key : map.keySet()) {
                fac[idx++] = key;
            }

            long MAX = 1 << fac.length;
            for (int i = 1; i < MAX; i++) {
                int tmp = i;
                idx = 0;
                long factor = 1;
                int cnt = 0;
                while (tmp > 0) {
                    if ((tmp & 1) == 1) {
                        cnt++;
                        factor *= fac[idx];
                    }
                    idx++;
                    tmp >>= 1;
                }
                if ((cnt & 1) == 1) {
                    sum = (sum + INF - sum((a + factor - 1) / factor, b / factor, factor)) % INF;
                } else {
                    sum = (sum + sum((a + factor - 1) / factor, b / factor, factor)) % INF;
                }
            }

            pw.print(sum);
            if (q > 0) {
                pw.println();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        boolean oj = System.getProperty("ONLINE_JUDGE") != null;
        if (!oj) {
            System.setIn(new FileInputStream("in.txt"));
//            System.setOut(new PrintStream("out.txt"));
        }
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StreamTokenizer(br);
        pw = new PrintWriter(new OutputStreamWriter(System.out));
        st.ordinaryChar('\''); //指定单引号、双引号和注释符号是普通字符
        st.ordinaryChar('\"');
        st.ordinaryChar('/');

        long t = System.currentTimeMillis();
        solve();
        if (!oj) {
            pw.println("[" + (System.currentTimeMillis() - t) + "ms]");
        }
        pw.flush();
    }

    private static int nextInt() throws IOException {
        st.nextToken();
        return (int) st.nval;
    }

    private static long nextLong() throws IOException {
        st.nextToken();
        return (long) st.nval;
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
