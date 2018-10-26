package format;

import java.util.Scanner;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/5/31 21:07
 * @see format
 */
public class KM {
    static class Km {
        static final int MAX_VAL = 1 << 9;
        static int n, m;
        static int W[][];
        static int lx[], ly[];
        static int left[];
        static boolean S[], T[];

        static void init(int n, int m) {
            Km.n = n;
            Km.m = m;
            W = new int[n + 1][n + 1];
            lx = new int[n + 1];
            ly = new int[n + 1];
            left = new int[n + 1];
            S = new boolean[n + 1];
            T = new boolean[n + 1];
        }

        static void add(int x, int y, int w) {
            W[x][y] = w;
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
            int a = Integer.MAX_VALUE;
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
    }


    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
    }
}
