package IEEE10;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class CheckersChallenge {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    static boolean empty(char[][] grid) {
        boolean flag = true;
        for (int i = 0; i < 8 && flag; i++) {
            for (int j = 0; j < 8 && flag; j++) {
                if (grid[i][j] == 'x') {
                    flag = false;
                }
            }
        }
        return flag;
    }

    static int dfs(char grid[][], int sx, int sy, boolean king, int d) {
        if (empty(grid)) {
            return 1;
        }
        int cnt = 0;
        if (!king) {
            for (int i = 0; i <= 2; i++) {
                int x = dir1[(i + 4) % 4] + sx;
                int y = dir1[(i + 5) % 4] + sy;
                if (!legal(x, y, 8, 8)) {
                    continue;
                }
                if (grid[x][y] == 'x') {
                    int tx = dir1[(i + 4) % 4] + x;
                    int ty = dir1[(i + 5) % 4] + y;
                    if (legal(tx, ty, 8, 8) && grid[tx][ty] != 'x') {
                        grid[x][y] = '.';
                        cnt += dfs(grid, tx, ty, tx == 0, (i + 4) % 4);
                        grid[x][y] = 'x';
                    }
                }
            }
        } else {
            for (int i = (d == -1 ? 0 : d - 1); i <= (d == -1 ? 3 : d + 1); i++) {
                int x = dir1[(i + 4) % 4] + sx;
                int y = dir1[(i + 5) % 4] + sy;
                while (legal(x, y, 8, 8) && grid[x][y] != 'x') {
                    x += dir1[(i + 4) % 4];
                    y += dir1[(i + 5) % 4];
                }
                if (!legal(x, y, 8, 8)) {
                    continue;
                }
                int tx = x;
                int ty = y;
                while (true) {
                    tx = dir1[(i + 4) % 4] + tx;
                    ty = dir1[(i + 5) % 4] + ty;
                    if (!legal(tx, ty, 8, 8) || grid[tx][ty] == 'x') {
                        break;
                    }
                    grid[x][y] = '.';
                    cnt += dfs(grid, tx, ty, true, (i + 4) % 4);
                    grid[x][y] = 'x';
                }
            }
        }
        return cnt;
    }

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {

        int T = Integer.parseInt(nextLine());
        while (T-- > 0) {
            char[][] grid = new char[8][8];
            int sx = -1, sy = -1;
            for (int i = 0; i < 8; i++) {
                grid[i] = nextLine().toCharArray();
                if (sx < 0) {
                    for (int j = 0; j < 8; j++) {
                        if (grid[i][j] == 'o') {
                            sx = i;
                            sy = j;
                        }
                    }
                }
            }

            int cnt = dfs(grid, sx, sy, sx == 0, -1);

            pw.print(cnt);

            if (T > 0) {
                nextLine();
                pw.println();
            }
        }
    }

    static int dir1[] = {0, -1, 0, 1, 0};
    static int dir2[][] = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}};


    static boolean legal(int x, int y, int X, int Y) {
        return x >= 0 && y >= 0 && x < X && y < Y;
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
