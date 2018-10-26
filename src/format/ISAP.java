package format;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Copyright © 2018 Chris. All rights reserved.
 *
 * @author Chris
 * 2018/10/17 10:20
 * @see format
 */
public class ISAP {
    static final int INF = 1000000007;

    static class Edge {
        int from, to, cap, flow;

        Edge(int from, int to, int cap, int flow) {
            this.from = from;
            this.to = to;
            this.cap = cap;
            this.flow = flow;
        }
    }

    static class Dinic {
        static int n, m, s, t;
        static List<Edge> edges = new ArrayList<>();
        static List<Integer>[] G;
        static boolean vis[];
        static int d[]; //the dist from source to i;
        static int cur[];//the index fo current edge
        static int p[];//the pre edge
        static int num[];//count of distance marks;

        static void addEdge(int from, int to, int cap) {
            edges.add(new Edge(from, to, cap, 0));
            edges.add(new Edge(to, from, 0, 0));
            G[from].add(edges.size() - 2);
            G[to].add(edges.size() - 1);
        }

        static boolean bfs() {
            vis = new boolean[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(s);
            d[s] = 0;
            vis[s] = true;
            while (!queue.isEmpty()) {
                int x = queue.poll();
                for (int i = 0; i < G[x].size(); i++) {
                    Edge e = edges.get(G[x].get(i));
                    if (!vis[e.to] && e.cap > e.flow) {//Consider only the arcs in the residual network
                        vis[e.to] = true;
                        d[e.to] = d[x] + 1;
                        queue.offer(e.to);
                    }
                }
            }
            return vis[t];
        }

        static int augment() {
            int x = t, a = INF;
            while (x != s) {
                Edge e = edges.get(p[x]);
                a = Math.min(a, e.cap - e.flow);
                x = e.from;
            }
            x = t;
            while (x != s) {
                edges.get(p[x]).flow += a;
                edges.get(p[x] ^ 1).flow -= a;
                x = edges.get(p[x]).from;
            }
            return a;
        }

        static int maxFlow(int s, int t) {
            Dinic.s = s;
            Dinic.t = t;
            int flow = 0;

            bfs();

            num = new int[n + 1];
            for (int i = 0; i < n; i++) {
                num[d[i]]++;
            }
            int x = Dinic.s;
            cur = new int[n + 1];
            while (d[s] < n) {
                if (x == t) {
                    flow += augment();
                    x = Dinic.s;
                }
                boolean ok = false;
                for (int i = cur[x]; i < G[x].size(); i++) {
                    Edge e = edges.get(G[x].get(i));
                    if (e.cap > e.flow && d[x] == d[e.to] + 1) {
                        ok = true;
                        p[e.to] = G[x].get(i);
                        cur[x] = i;
                        x = e.to;
                        break;
                    }
                }
                if (!ok) {
                    int m = n - 1;
                    for (int i = 0; i < G[x].size(); i++) {
                        Edge e = edges.get(G[x].get(i));
                        if (e.cap > e.flow) {
                            m = Math.min(m, d[e.to]);
                        }
                        if (--num[d[x]] == 0) {
                            break;
                        }
                        num[d[x] = m + 1]++;
                        cur[x] = 0;
                        if (x != s) {
                            x = edges.get(p[x]).from;
                        }
                    }
                }
            }

            return flow;
        }
    }


}
