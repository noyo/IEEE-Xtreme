package IEEE10;

import javafx.scene.input.DataFormat;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class FoodTruck {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

    static final int INF = 1000000007;

    static final double R = 6378.137f;

    static double count(double lat1, double long1, double lat2, double long2) {
        return 2 * R * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1 - lat2) / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((long1 - long2) / 2), 2)));
    }

    static String getTime(String s) {
        return s.substring(6, 10)
                + s.substring(0, 2)
                + s.substring(3, 5)
                + s.substring(11);
    }

    private static void solve() throws IOException {
        Map<Long, String> phone = new HashMap<>();
        List<Long> ans = new ArrayList<>();

        String ss[] = nextSS(",");
        double lat1 = Double.parseDouble(ss[0]) / 180 * Math.PI;
        double long1 = Double.parseDouble(ss[1]) / 180 * Math.PI;
        double r = Double.parseDouble(nextLine());
        nextLine();
        String s;
        while ((s = br.readLine()) != null) {
            ss = s.split(",");
            long l = Long.parseLong(ss[ss.length - 1]);
            String time = getTime(ss[0]);
            if (phone.containsKey(l) && phone.get(l).compareTo(time) >= 0) {
                continue;
            }
            double d = count(lat1, long1, Double.parseDouble(ss[ss.length - 3]) / 180 * Math.PI
                    , Double.parseDouble(ss[ss.length - 2]) / 180 * Math.PI);
            if (d <= r) {
                phone.put(l, time);
            } else {
                phone.remove(l);
            }
        }
        for (long key : phone.keySet()) {
            ans.add(key);
        }
        ans.sort(Comparator.naturalOrder());
        for (int i = 0; i < ans.size(); i++) {
            pw.print(ans.get(i));
            if (i < ans.size() - 1) {
                pw.print(",");
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
