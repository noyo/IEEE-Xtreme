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
public class TheGolddiggers {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;
    static final int MOD = 1000000007;
    static List<int[]> perm = new ArrayList<>();

    static boolean[] vis1 = new boolean[8];

    static void dfs(int a[], int i) {
        if (i >= 7) {
            int tmp[] = new int[7];
            System.arraycopy(a, 0, tmp, 0, 7);
            perm.add(tmp);
            return;
        }
        for (int j = 1; j <= 7; j++) {
            if (vis1[j]) {
                continue;
            }
            vis1[j] = true;
            a[i] = j;
            dfs(a, i + 1);
            vis1[j] = false;
        }
    }

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        dfs(new int[7], 0);
        int n = Integer.parseInt(nextLine());
        int gold[] = new int[8];
        for (int i = 1; i <= 7; i++) {
            gold[i] = n - i;
        }
        int dist[][] = new int[n][n];
        List<Integer>[] edges = new List[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            String ss[] = nextSS(" ");
            for (int j = 0; j < ss.length; j++) {
                int to = Integer.parseInt(ss[j]);
                edges[i].add(to);
                edges[to].add(i);
            }
        }
        for (int i = 0; i < 8; i++) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(gold[i]);
            boolean vis[] = new boolean[n];
            vis[gold[i]] = true;
            int cnt = 1;
            while (!queue.isEmpty()) {
                int x = queue.size();
                while (x-- > 0) {
                    int from = queue.poll();
                    List<Integer> es = edges[from];
                    for (int j = 0; j < es.size(); j++) {
                        int to = es.get(j);
                        if (vis[to]) {
                            continue;
                        }
                        vis[to] = true;
                        dist[gold[i]][to] = cnt;
                    }
                }
                cnt++;
            }
        }
        int max = 0;
        for (int i = 0; i < 5040; i++) {
            int a[] = perm.get(i);
            int pre0 = 0;
            int d0[] = new int[8];
            boolean ok = true;
            for (int j = 0; j < 7; j++) {
                int idx = gold[a[j]];
                if (dist[idx][gold[pre0]] == 0) {
//                    ok = false;
                    break;
                }
                d0[a[j]] = d0[pre0] + dist[idx][gold[pre0]];
                pre0 = a[j];
            }
            if (!ok) {
                continue;
            }
            int curMax = 7;
            for (int j = 0; j < 5040; j++) {
                if (j == i) {
                    continue;
                }
                boolean can = true;
                int b[] = perm.get(j);
                int d1[] = new int[8];
                int cur = 0;
                int pre1 = 0;
                for (int k = 0; k <= 6; k++) {
                    int idx = gold[b[k]];
                    if (dist[idx][gold[pre1]] == 0) {
//                        can = false;
                        break;
                    }
                    d1[b[k]] = d1[pre1] + dist[idx][gold[pre1]];
                    pre1 = b[k];
                }
                if (!can) {
                    continue;
                }
                for (int k = 1; k <= 7; k++) {
                    if (d0[k] > 0 && d0[k] <= d1[k]) {
                        cur++;
                    }
                }
                curMax = Math.min(cur, curMax);
            }
            max = Math.max(max, curMax);
        }
        pw.print(max);
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
