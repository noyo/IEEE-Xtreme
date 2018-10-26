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
public class GottaCatchEmAll {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;
    static final int MOD = 1000000007;

    static int n, m, ans;
    static int dist[];
    static int c[], d[], pre[];
    static boolean vst[];
    static List<Integer> edges[];
    static Edge[] a;

    static class Edge {
        int from;
        int to;
        int w;

        Edge(int a, int b, int c) {
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
        pre = new int[m / n + 1];
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

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        int t = nextInt();
        while (t-- > 0) {
            int L = nextInt();
            n = nextInt();
            int k = nextInt();
            int T = nextInt();
            m = n * L;
            a = new Edge[m + 1];
            c = new int[n + 1];
            d = new int[L + 1];
            dist = new int[n + 1];
            Arrays.fill(dist, -1);
            edges = new List[n + 1];
            vst = new boolean[n + 1];
            int tmp[][] = new int[105][105];
            int min[] = new int[105];
            Arrays.fill(min, 500);

            a[0] = new Edge(0, 0, -1);

            int cur = 1;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= L; j++) {
                    tmp[i][j] = nextInt();
                    a[cur++] = new Edge(i, j, tmp[i][j]);
                    min[i] = Math.min(tmp[i][j], min[i]);
                }
            }
            Arrays.sort(a, Comparator.comparingDouble(o -> o.w));
            for (int i = 1; i <= n; i++) {
                edges[i] = new ArrayList<>();
            }
            pre();
            ans = 0;

            int minAns = 0;
            for (int i = 1; i <= L * n; i++) {
                add(a[i]);
                if (dist[ans] < 0) {
                    dist[ans] = a[i].w;
                }
                if (ans == k) {
                    break;
                }
            }
            minAns = dist[ans];
            for (int i = 1; i <= L; i++) {
                int LL = L + 1;
                m = n * LL;
                a = new Edge[m + 1];
                c = new int[n + 1];
                d = new int[LL + 1];
                dist = new int[n + 1];
                Arrays.fill(dist, -1);
                edges = new List[n + 1];
                vst = new boolean[n + 1];

                a[0] = new Edge(0, 0, -1);

                cur = 1;
                for (int w = 1; w <= n; w++) {
                    tmp[w][LL] = tmp[w][i] + T;
                    for (int j = 1; j <= LL; j++) {
                        a[cur++] = new Edge(w, j, tmp[w][j]);
                    }
                }
                Arrays.sort(a, Comparator.comparingDouble(o -> o.w));
                for (int w = 1; w <= n; w++) {
                    edges[w] = new ArrayList<>();
                }
                pre();
                ans = 0;

                for (int w = 1; w <= LL * n; w++) {
                    add(a[w]);
                    if (dist[ans] < 0) {
                        dist[ans] = a[w].w;
                    }
                    if (ans == k) {
                        break;
                    }
                }
                minAns = Math.min(minAns, dist[ans]);
            }

            pw.print(minAns);

            if (t > 0) {
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
