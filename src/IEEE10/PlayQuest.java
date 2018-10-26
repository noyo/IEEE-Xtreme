package IEEE10;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/7/9 15:33
 * @see format
 */
public class PlayQuest {

    private static BufferedReader br;
    private static StreamTokenizer st;
    private static PrintWriter pw;

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

    static class Quest {
        int type;
        char color;
        int k;
        int next = 0;

        Quest(int type, char color, int k) {
            this.type = type;
            this.color = color;
            this.k = k;
        }
    }

    @SuppressWarnings("unchecked")
    private static void solve() throws IOException {
        int T = nextInt();
        char map[] = {'r', 'b', 'g'};
        while (T-- > 0) {
            int q = nextInt();
            int l = nextInt();
            int MAX = (int) Math.pow(3, 10);
            nextLine();
            int color[][] = new int[11][128];
            List<Quest>[] quests = new List[q];
            boolean truth[] = new boolean[q];
            for (int i = 0; i < q; i++) {
                quests[i] = new ArrayList<>();
                String ss[] = nextSS(" ");
                int n = ss.length;
                int cur = 0;
                while (cur < n) {
                    Quest quest;
                    if (ss[cur].equals("color")) {
                        quest = new Quest(0, ss[cur + 2].charAt(0), Integer.parseInt(ss[cur + 1]));
                    } else {
                        quest = new Quest(1, ss[cur + 1].charAt(0), Integer.parseInt(ss[cur + 2]));
                    }
                    cur += 3;
                    if (cur < n) {
                        if (ss[cur].equals("or")) {
                            quest.next = 1;
                        }
                        cur++;
                    }
                    quests[i].add(quest);
                }
                truth[i] = nextLine().equals("yes");
            }

            while (--MAX >= 0) {
                char c[] = new char[11];
                int cnt[] = new int[128];
                int val = MAX;
                int lie = 0;
                for (int i = 1; i <= 10; i++) {
                    c[i] = map[val % 3];
                    val /= 3;
                    cnt[c[i]]++;
                }
                for (int i = 0; i < q; i++) {
                    List<Quest> questList = quests[i];
                    boolean flag = true;
                    int next = 0;
                    for (int j = 0; j < questList.size(); j++) {
                        Quest quest = questList.get(j);
                        if (quest.type == 0) {
                            if (next == 0) {
                                flag &= (c[quest.k] == quest.color);
                            } else {
                                flag |= (c[quest.k] == quest.color);
                            }
                        } else {
                            if (next == 0) {
                                flag &= (cnt[quest.color] == quest.k);
                            } else {
                                flag |= (cnt[quest.color] == quest.k);
                            }
                        }
                        next = quest.next;
                    }
                    if (flag ^ truth[i]) {
                        lie++;
                    }
                }
                if (lie == l) {
                    for (int i = 1; i <= 10; i++) {
                        color[i][c[i]] = 1;
                    }
                }
            }

            for (int i = 1; i <= 10; i++) {
                if (color[i]['r'] == 1) {
                    pw.print('r');
                }
                if (color[i]['g'] == 1) {
                    pw.print('g');
                }
                if (color[i]['b'] == 1) {
                    pw.print('b');
                }
                if (i <= 9) {
                    pw.print(" ");
                }
            }

            if (T > 0) {
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
