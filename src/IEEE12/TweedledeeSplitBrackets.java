package IEEE12;

import java.io.*;
import java.util.*;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class TweedledeeSplitBrackets {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;
    static final int MOD = 1000000007;
    static Map<Character, Integer> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        map.put('[', 0);
        map.put('(', 1);
        map.put(']', 2);
        map.put(')', 3);

        char[] ch = nextLine().toCharArray();
        int n = ch.length;
        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = map.get(ch[i]);
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (a[i] < 2) {
                list.add(i);
            }
        }
        int quart = list.size();
        if (quart * 2 != n || ch.length % 4 != 0) {
            pw.print("impossible");
            return;
        }
        boolean ok = true;
        int MAX = 1 << quart;
        for (int i = MAX - 1; i >= 0; i--) {
            int bit[] = new int[quart];
            int cnt = 0;
            int tmp = i;
            int cur = quart - 1;
            while (tmp > 0) {
                bit[cur--] = tmp % 2;
                tmp >>= 1;
                if (tmp % 2 == 1) cnt++;
            }
            if (cnt != quart / 2) {
                continue;
            }
            boolean vis[] = new boolean[n];
            boolean vis2[] = new boolean[n];

            for (int k = 0; k < ch.length; k++) {
                if (a[k] < 2) {
                    continue;
                }
                for (int j = k - 1; j >= 0; j--) {
                    if (a[j] < 2 && vis[j] && !vis2[j]) {
                        if ((a[j] % 2) != (a[k] % 2)) {
                            break;
                        }
                        vis[k] = true;
                        vis2[j] = true;
                        cur++;
                        break;
                    }
                }
                if (cur == quart / 2) {
                    break;
                }
            }
            if (cur < quart / 2) {
                ok = false;
                continue;
            }
            int l1 = 0;
            int l2 = 0;
            for (int k = 0; k < ch.length; k++) {
                if (vis[k]) {
                    continue;
                }
                if (ch[k] == '[') {
                    l1++;
                } else if (ch[k] == '(') {
                    l2++;
                } else if (ch[k] == ']') {
                    l1--;
                    if (l1 < 0) {
                        ok = false;
                        break;
                    }
                } else {
                    l2--;
                    if (l2 < 0) {
                        ok = false;
                        break;
                    }
                }
            }
            if (l1 > 0 || l2 > 0) {
                ok = false;
            }
            if (ok) {
                for (int k = 0; k < ch.length; k++) {
                    int v = vis[k] ? 1 : 2;
                    pw.print(v);
                    if (k < ch.length - 1) {
                        pw.print(" ");
                    }
                }

                ok = dfs(bit, 0);
                 break;
            }
        }

        if (!ok) {
            pw.print("impossible");
        }
    }

    private static boolean dfs(int[] bit, int cur) {
        return false;
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

    // 18's prime:154590409516822759
    // 19's prime:2305843009213693951 (Mersenne primes)
    // 19's prime:4384957924686954497
    static boolean isPrime(long n) { //determines if n is a prime number
        int p[] = {2, 3, 5, 233, 331};
        int pn = p.length;
        long s = 0, t = n - 1;//n - 1 = 2^s * t
        while ((t & 1) == 0) {
            t >>= 1;
            ++s;
        }
        for (int i = 0; i < pn; ++i) {
            if (n == p[i]) {
                return true;
            }
            long pt = pow(p[i], t, n);
            for (int j = 0; j < s; ++j) {
                long cur = llMod(pt, pt, n);
                if (cur == 1 && pt != 1 && pt != n - 1) {
                    return false;
                }
                pt = cur;
            }
            if (pt != 1) {
                return false;
            }
        }
        return true;
    }

    static long llMod(long a, long b, long mod) {
        return (a * b - (long) ((double) a / mod * b + 0.5) * mod + mod) % mod;
//        long r = 0;
//        a %= mod;
//        b %= mod;
//        while (b > 0) {
//            if ((b & 1) == 1) {
//                r = (r + a) % mod;
//            }
//            b >>= 1;
//            a = (a << 1) % mod;
//        }
//        return r;
    }

    static int pow(long a, long n) {
        long ans = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans = (ans * a) % MOD;
            }
            a = (a * a) % MOD;
            n >>= 1;
        }
        return (int) ans;
    }

    static int pow(long a, long n, long mod) {
        long ans = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans = llMod(ans, a, mod);
            }
            a = llMod(a, a, mod);
            n >>= 1;
        }
        return (int) ans;
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
            for (int j = i * i; j <= n; j += i << 1) {
                p[j] = false;
            }
        }
        return p;
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
                ans = (ans * a) % MOD;
            }
            a = (a * a) % MOD;
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
            top = (top * i) % MOD;
        }
        for (int i = 1; i <= m; i++) {
            bot = (bot * i) % MOD;
        }

        return (int) ((top * pow(bot, MOD - 2)) % MOD);
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


    private static String next(int len) throws IOException {
        st.nextToken();
        return st.sval;
//        char ch[] = new char[len];
//        int cur = 0;
//        char c;
//        int k = br.read();
//        if (k == -1) {
//            throw new NullPointerException();
//        }
//        while ((c = (char) k) == '\n' ||  c == '\r' || c == ' ' || c == '\t') {
//            k = br.read();
//        }
//        do {
//            ch[cur++] = c;
//        } while (!(((k = br.read()) == -1) || (c = (char) k) == '\n' || c == '\r' || c == ' ' || c == '\t'));
//        return String.valueOf(ch, 0, cur);
    }

    private static int nextInt() throws IOException {
        st.nextToken();
        return (int) st.nval;
    }

    private boolean isTrash(int c) {
        return c < 33 || c > 126;
    }

    long nextLong() throws IOException {
        long ret = 0;
        char c;
        int b = br.read();
        if (b == -1) {
            throw new NullPointerException();
        }
        while ((c = (char) b) == '\n' || c == '\r' || c == ' ' || c == '\t') {
            b = br.read();
        }

        if (b != '-' && (b < '0' || b > '9')) {
            throw new InputMismatchException();
        }
        boolean neg = false;
        if (b == '-') {
            neg = true;
            b = br.read();
        }
        while (true) {
            if (b >= '0' && b <= '9') {
                ret = ret * 10 + (b - '0');
            } else {
                if (b != -1 && !isTrash(b)) {
                    throw new InputMismatchException();
                }
                return neg ? -ret : ret;
            }
            b = br.read();
        }
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
