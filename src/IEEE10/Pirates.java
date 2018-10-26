package IEEE10;

import java.io.*;
import java.util.*;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class Pirates {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    static boolean legal(int x, int y) {
        return x > 0 && y > 0 && x <= N && y <= M;
    }

    static int N, M;
    static int dir[][] = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}};
    static int map[][];
    static char grid[][];
    static List<Integer> V;
    static Map<Integer, Set<Integer>> edges;
    static int dist[];
    static int parent[];

    static void bfs(boolean vis[][], char[][] grid, int x, int y, int ll, char obj) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{x, y});
        while (!queue.isEmpty()) {
            int n = queue.size();
            while (n-- > 0) {
                int p[] = queue.poll();
                for (int i = 0; i < 8; i++) {
                    int r = p[0] + dir[i][0];
                    int c = p[1] + dir[i][1];
                    if (!legal(r, c) || vis[r][c] || grid[r][c] != obj) {
                        continue;
                    }
                    vis[r][c] = true;
                    map[r][c] = ll;
                    queue.offer(new int[]{r, c});
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static void init(char[][] grid) {
        V = new ArrayList<>();
        edges = new HashMap<>();
        boolean vis[][] = new boolean[N + 1][M + 1];
        int v = 1;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if (grid[i][j] != 'O' || vis[i][j]) {
                    continue;
                }
                V.add(v);
                edges.put(v, new HashSet<>());
                vis[i][j] = true;
                map[i][j] = v;
                bfs(vis, grid, i, j, v, 'O');
                v += 2;
            }
        }

        int k = 0;
        v = 2;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if (vis[i][j] || grid[i][j] == 'O') {
                    continue;
                }
                V.add(v);
                k++;
                edges.put(v, new HashSet<>());
                vis[i][j] = true;
                map[i][j] = v;
                bfs(vis, grid, i, j, v, '~');
                v += 2;
            }
        }
        dist = new int[k + 1];
        parent = new int[k + 1];

        vis = new boolean[N + 1][M + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                if (vis[i][j]) {
                    continue;
                }
                Queue<int[]> queue = new LinkedList<>();
                queue.offer(new int[]{i, j});
                while (!queue.isEmpty()) {
                    int x = queue.size();
                    while (x-- > 0) {
                        int p[] = queue.poll();
                        for (int z = 0; z < 8; z++) {
                            int r = p[0] + dir[z][0];
                            int c = p[1] + dir[z][1];
                            if (!legal(r, c) || vis[r][c]) {
                                continue;
                            }
                            if (map[r][c] != map[p[0]][p[1]]) {
                                edges.get(map[p[0]][p[1]]).add(map[r][c]);
                                edges.get(map[r][c]).add(map[p[0]][p[1]]);
                                continue;
                            }
                            vis[r][c] = true;
                            queue.offer(new int[]{r, c});
                        }
                    }
                }
            }
        }
    }

    private static void solve() throws IOException {

        N = nextInt();
        M = nextInt();
        int T = nextInt();
        grid = new char[N + 1][M + 1];
        map = new int[N + 1][M + 1];
        nextLine();
        for (int i = 1; i <= N; i++) {
            char ch[] = nextLine().toCharArray();
            System.arraycopy(ch, 0, grid[i], 1, M);
        }

        init(grid);
        count(2);

        while (T-- > 0) {
            int x1 = nextInt();
            int y1 = nextInt();
            int x2 = nextInt();
            int y2 = nextInt();

            int from = map[x1][y1];
            int to = map[x2][y2];
            int cnt = count(from, to);
            pw.print(cnt);

            if (T > 0) {
                pw.println();
            }
        }
    }

    static int count(int from, int to) {
        from >>= 1;
        to >>= 1;
        int cnt = 0;
        while (dist[from] > dist[to]) {
            cnt++;
            from = parent[from];
        }
        while (dist[from] < dist[to]) {
            cnt++;
            to = parent[to];
        }
        while (from != to) {
            from = parent[from];
            to = parent[to];
            cnt += 2;
        }

        return cnt;
    }

    private static void count(int from) {
        int pre = from >> 1;
        Set<Integer> vis = new HashSet<>(V.size());
        vis.add(from);
        Queue<int[]> queue = new LinkedList<>();
        for (int t : edges.get(from)) {
            if (vis.contains(t)) {
                continue;
            }
            vis.add(t);
            queue.offer(new int[] {t, pre, dist[from]});
        }
        while (!queue.isEmpty()) {
            int x = queue.size();
            while (x-- > 0) {
                int p[] = queue.poll();
                for (int t : edges.get(p[0])) {
                    if (vis.contains(t)) {
                        continue;
                    }
                    vis.add(t);
                    int d;
                    if (t % 2 == 0) {
                        d = dist[t >> 1] = p[2] + 1;
                        parent[t >> 1] = p[1];
                    } else {
                        d = p[2];
                    }
                    queue.offer(new int[] {t, p[0] >> 1, d});
                }
            }
        }
    }

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

    private static String next(int len) throws IOException {
        char ch[] = new char[len];
        int cur = 0;
        char c;
        int k = br.read();
        if (k == -1) {
            throw new NullPointerException();
        }
        while ((c = (char) k) == '\n' || c == '\r' || c == ' ' || c == '\t') {
            k = br.read();
        }
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
