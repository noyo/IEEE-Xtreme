package IEEE10;

import java.io.*;
import java.util.*;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/10/16 11:23
 * @see format
 */
public class FindingShelter {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;
    static final int MOD = 1000000007;

    static int n, m, ans;
    static double dist[];
    static int c[], d[], pre[];
    static boolean vst[];
    static List<Integer> edges[];
    static Edge[] a;
    static double cnt;

    static class Edge {
        int from;
        int to;
        double w;

        Edge(int a, int b, double c) {
            this.from = a;
            this.to = b;
            this.w = c;
        }

        double cap, flow;

        Edge(int from, int to, double cap, double flow) {
            this.from = from;
            this.to = to;
            this.cap = cap;
            this.flow = flow;
        }
    }

    static void dfs1(int x) {
        if (x == 0 || vst[x]) {
            return;
        }
        vst[x] = true;
        int i;
        for (i = 0; i < edges[x].size(); i++) {
            int A = edges[x].get(i);
            if (pre[A] == 0) {
                pre[A] = x;
            }
            dfs1(d[A]);
        }
    }

    static void pre() {
        vst = new boolean[n + 1];
        pre = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            if (c[i] == 0) {
                dfs1(i);
            }
        }
    }

    static int dfs(int x, int y) {
        if (vst[x]) {
            return 0;
        }
        vst[x] = true;
        for (int i = y; i < edges[x].size(); i++) {
            int A = edges[x].get(i);
            if (pre[A] == 0) {
                pre[A] = x;
            }
            if (d[A] == 0 || dfs(d[A], 0) > 0) {
                c[x] = A;
                d[A] = x;
                return 1;
            }
        }
        return 0;
    }

    static void dfs2(int x) {
        if (x == 0) {
            return;
        }
        int A = pre[x];
        int B = c[A];
        c[A] = x;
        d[x] = A;
        dfs2(B);
    }

    static void add(Edge edge) {
        int x = edge.from;
        int y = edge.to;
        edges[x].add(y);
        if (vst[x]) {
            vst[x] = false;
            int z = c[x];
            if (dfs(x, edges[x].size() - 1) > 0) {
                ans++;
                dfs2(z);
                pre();
            }
        }
    }

    static double dst(double x[], double y[]) {
        double s = Math.pow(x[0] - y[0], 2) + Math.pow(x[1] - y[1], 2);
        return Math.sqrt(s);
    }


    static double xx[][];
    static double yy[][];

    static void init() throws IOException {
        xx = new double[n + 1][2];
        yy = new double[n + 1][2];
        for (int i = 1; i <= n; i++) {
            xx[i][0] = nextDouble();
            xx[i][1] = nextDouble();
        }
        for (int i = 1; i <= n; i++) {
            yy[i][0] = nextDouble();
            yy[i][1] = nextDouble();
        }
        int cur = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                a[cur++] = new Edge(i, j, dst(xx[i], yy[j]));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        n = nextInt();
        m = n * n;
        a = new Edge[m + 1];
        c = new int[n + 1];
        d = new int[n + 1];
        dist = new double[n + 1];
        Arrays.fill(dist, -1);
        edges = new List[n + 1];
        vst = new boolean[n + 1];

        a[0] = new Edge(0, 0, -1);
        init();
        Arrays.sort(a, Comparator.comparingDouble(o -> o.w));
        for (int i = 1; i <= n; i++) {
            edges[i] = new ArrayList<>();
        }
        cnt = 0;
        pre();
        ans = 0;
        for (int i = 1; i <= m; i++) {
            add(a[i]);
            if (dist[ans] < 0) {
                dist[ans] = a[i].w;
            }
            if (ans == n) {
                while (i < m && a[i + 1].w == a[i].w) {
                    i++;
                }
                Km.init(n, i);
                for (int j = 1; j <= i; j++) {
                    Km.add(a[j].from, a[j].to, a[j].w);
                }
                Km.km();
                cnt = Km.count();
                break;
            }
        }
        pw.println(dist[n]);
        pw.print(cnt);
    }

    static class Km {
        static final double MAX_VAL = 1e10;
        static int n, m;
        static double W[][];
        static double lx[], ly[];
        static int left[];
        static boolean S[], T[];

        static void init(int n, int m) {
            Km.n = n;
            Km.m = m;
            W = new double[n + 1][n + 1];
            lx = new double[n + 1];
            ly = new double[n + 1];
            left = new int[n + 1];
            S = new boolean[n + 1];
            T = new boolean[n + 1];
        }

        static void add(int x, int y, double w) {
            W[x][y] = MAX_VAL - w;
        }

        static boolean match(int i) {
            S[i] = true;
            for (int j = 1; j <= n; j++) {
                if (lx[i] + ly[j] == W[i][j] && !T[j]) {
                    T[j] = true;
                    if (left[j] == 0 || match(left[j])) {
                        left[j] = i;
                        return true;
                    }
                }
            }
            return false;
        }

        static void update() {
            double a = MAX_VAL;
            for (int i = 1; i <= n; i++) {
                if (S[i]) {
                    for (int j = 1; j <= n; j++) {
                        if (!T[j]) {
                            a = Math.min(a, lx[i] + ly[j] - W[i][j]);
                        }
                    }
                }
            }
            for (int i = 1; i <= n; i++) {
                if (S[i]) {
                    lx[i] -= a;
                }
                if (T[i]) {
                    ly[i] += a;
                }
            }
        }

        static void km() {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    lx[i] = Math.max(lx[i], W[i][j]);
                }
            }

            for (int i = 1; i <= n; i++) {
                for (; ; ) {
                    for (int j = 1; j <= n; j++) {
                        S[j] = T[j] = false;
                    }
                    if (match(i)) {
                        break;
                    } else {
                        update();
                    }
                }
            }
        }
        
        static double count() {
            double ans = 0;
            for (int i = 1; i <= n; i++) {
                if (left[i] > 0) {
                    ans += MAX_VAL - W[left[i]][i];
                }
            }
            return ans;
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

    // 18's prime：154590409516822759
    // 19's prime：2305843009213693951 (Mersenne primes)
    // 19's prime：4384957924686954497
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
        char ch[] = new char[len];
        int cur = 0;
        char c;
        int k = br.read();
        if (k == -1) {
            throw new NullPointerException();
        }
        while ((c = (char) k) == '\n' || c == '\r' || c == ' ' || c == '\t') ;
        do {
            ch[cur++] = c;
        } while (!(((k = br.read()) == -1) || (c = (char) k) == '\n' || c == '\r' || c == ' ' || c == '\t'));
        return String.valueOf(ch, 0, cur);
    }

    private static int nextInt() throws IOException {
        st.nextToken();
        return (int) st.nval;
    }

    private static long nextLong() throws IOException {
        return Long.parseLong(nextLine());
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
