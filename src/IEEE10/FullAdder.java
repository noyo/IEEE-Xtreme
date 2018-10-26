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
public class FullAdder {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        String lin1 = nextLine();
        String lin2 = nextLine();
        String lin3 = nextLine();
        String lin4 = nextLine();
        String ss[] = lin1.split(" ");
        int d = Integer.parseInt(ss[0]);
        Map<Character, Integer> map1 = new HashMap<>((int) 1e7);
        map1.put(' ', 0);
        map1.put('+', 0);
        for (int i = 0; i < d; i++) {
            map1.put(ss[1].charAt(i), i);
        }
        char[] add1 = lin2.toCharArray();
        char[] add2 = lin3.toCharArray();
        int l1 = add1.length;
        int l2 = add2.length;
        int r = 0;
        char[] ans = new char[lin4.length()];
        int n = ans.length;
        int i = 1;
        for (; i <= l1 && i <= l2; i++) {
            ans[n - i] = ss[1].charAt((r + map1.get(add1[l1 - i]) + map1.get(add2[l2 - i])) % d);
            r = ((r + map1.get(add1[l1 - i]) + map1.get(add2[l2 - i]))) / d;
        }
        while (i <= l1 && r > 0) {
            ans[n - i] = ss[1].charAt((r + map1.get(add1[l1 - i])) % d);
            r = ((r + map1.get(add1[l1 - i]))) / d;
            i++;
        }
        while (i <= l2 && r > 0) {
            ans[n - i] = ss[1].charAt((r + map1.get(add2[l2 - i])) % d);
            r = ((r + map1.get(add2[l2 - i]))) / d;
            i++;
        }
        while (i <= l1) {
            ans[n - i] = add1[l1 - i];
        }
        while (i <= l2) {
            ans[n - i] = add2[l2 - i];
        }
        if (r > 0) {
            ans[n - i] = ss[1].charAt(r);
        }
        if (n - i < 0 || ans[n - i] == ' ') {
            i--;
        }
        for (i = n - i; i < n - 1; i++) {
            if (ans[i] == '0' || ans[i] == ss[1].charAt(0)) {
                ans[i] = ' ';
            } else {
                break;
            }
        }
        pw.println(lin1);
        pw.println(lin2);
        pw.println(lin3);
        pw.println(lin4);
        pw.print(String.valueOf(ans));
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
