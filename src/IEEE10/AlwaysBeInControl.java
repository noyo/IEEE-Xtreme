package IEEE10;

import java.io.*;
import java.util.Map;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class AlwaysBeInControl {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        double A[] = {0, 0, 1.880, 1.023, 0.729, 0.577, 0.483, 0.419, 0.373, 0.337, 0.308};
        int T = nextInt();
        while (T-- > 0) {
            int x = nextInt();
            int n = nextInt();
            int a[] = new int[x];
            double sum[] = new double[x + 1];
            for (int i = 0; i < x; i++) {
                a[i] = nextInt();
                sum[i + 1] = sum[i] + a[i];
            }
            double average;
            double range = 0;
            for (int i = 0; i < (x + n - 1) / n; i++) {
                double min = 20000;
                double max = -20000;
                for (int j = i * n; j < Math.min((i + 1) * n, x); j++) {
                    min = Math.min(min, a[j]);
                    max = Math.max(max, a[j]);
                }
                range += max - min;
            }
            average = sum[x] / x;
            range /= (x + n - 1) / n;
            double up = average + A[n] * range;
            double down = average - A[n] * range;
            double sigma = (up - down) / 6;
            boolean flag = true;
            int cnt1 = 0;
            int cnt2 = 0;
            for (int i = 0; i < x; i++) {
                if (a[i] > up || a[i] < down) {
                    flag = false;
                    break;
                }
                if (a[i] > sigma * 2 || a[i] < -sigma * 2) {
                    cnt2++;
                }
                if (a[i] > sigma || a[i] < -sigma) {
                    cnt1++;
                }
            }
            if (cnt2 < x / 3.0 * 2.0 || cnt1 < x / 5.0 * 4.0) {
                flag = false;
            }

            if (flag) {
                pw.print("In Control");
            } else {
                pw.print("Out of Control");
            }

            if (T > 0) {
                pw.println();
            }
        }
    }

    static int dir1[] = {0, -1, 0, 1, 0};
    static int dir[][] = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}};

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

    static int pow(long a, long n) {
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

    static int gcd(int a, int b) {
        if (a < b) {
            a ^= b;
            b ^= a;
            a ^= b;
        }
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    private static long[][] initC(int n) {
        long c[][] = new long[n][n];

        for (int i = 0; i < n; i++) {
            c[i][0] = 1;
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
            }
        }
        return c;
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

    /**
     * ps: n >= m, choose m from n;
     */
    private static int c(long n, long m) {
        if (m > n) {
            n ^= m;
            m ^= n;
            n ^= m;
        }
        m = Math.min(m, n - m);

        long top = 1;
        long bot = 1;
        for (long i = n - m + 1; i <= n; i++) {
            top = (top * i) % INF;
        }
        for (int i = 1; i <= m; i++) {
            bot = (bot * i) % INF;
        }

        return (int) ((top * pow(bot, INF - 2)) % INF);
    }

    static boolean even(long n) {
        return (n & 1) == 0;
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
        st.ordinaryChar('\'');
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
