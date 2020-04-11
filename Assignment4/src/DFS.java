/***********************************************************************************************************************
 *
 *  Pace University
 *  Spring 2020
 *  Algorithms and Computing Theory
 *
 *  Course: CS 242
 *  Team members: Karl Eshwer
 *  Collaborators: NONE
 *  References:
 *
 *  Assignment: 4
 *  Problem: Evaluate experimentally the performance of an efficient implementation of DFS.
 *  Description: the time efficiency of algorithms that use DFS as a graph traversal depends heavily on implementing DFS
 *               efficiently. Given a directed graph G = {V,E} encoded as an adjacency list, an efficient implementation
 *               of DFS runs in O(|V| + |E|) steps.
 *
 *  Input: number of nodes & number of edges
 *  Output: The running time
 *
 *  Visible data fields:
 *      static int WHITE = 0;
 *      static int GRAY = 1;
 *      static int BLACK = 2;
 *      static int NIL = -1;
 *      static int[] color;
 *      static int[] pi;
 *      static int[] a;
 *      static int[] b;
 *      static int time = 0;
 *
 *  Visible methods:
 *  public static class DirectedGraph
 *  static void dfs(DirectedGraph G)
 *  static void dfs_visit(DirectedGraph G, int u)
 *  static int[] createArray(int length, int initialValue)
 *  static DirectedGraph createGraphRandomly(int numOfNodes, int numOfEdges)
 *  public static void main(String[] args)
 *
 *
 *  Remarks
 *  ------
 *  2)                   Table of values used:
 *
 * |            | |E|=|V|−1 | |E|=⌊(|V|−1)^3/2⌋ | |E|=(|V|−1)^2 |
 * |------------|-----------|-------------------|---------------|
 * | |V| = 10   | E = 9     | E = 27            | E = 81        |
 * | |V| = 100  | E = 99    | E = 985           | E = 9081      |
 * | |V| = 1000 | E = 999   | E = 31575         | E = 998001    |
 *
 *
 *                Table of running times in  nanoseconds:
 *
 * |            | |E|=|V|−1 | |E|=⌊(|V|−1)^3/2⌋ | |E|=(|V|−1)^2 |
 * |------------|-----------|-------------------|---------------|
 * | |V| = 10   | 191158    | 237572            | 263350        |
 * | |V| = 100  | 486782    | 781807            | 2071114       |
 * | |V| = 1000 | 2332028   | 7100816           | 58681359      |
 *
 *
 * 3) The formula I was able to approximate from my experimental finding is as follows:
 *
 *      18213.397 V + 1002.67 E
 *
 *    This is in accordance with O(|V|+|E|) as with any increase in V and E we see an increase in the running time
 *    which is expected from the results seen in the table above.
 *
 **********************************************************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DFS {

     static int WHITE = 0;
     static int GRAY = 1;
     static int BLACK = 2;
     static int NIL = -1;

     static int[] color;
     static int[] pi;
     static int[] a;
     static int[] b;

     static int time = 0;

    public static class DirectedGraph {

        int numberOfVertices;
        private ArrayList<ArrayList<Integer>> adjaList;

        public DirectedGraph(int numOfVertices) {
            this.numberOfVertices = numOfVertices;

            //Initialize an adjacency list
            adjaList = new ArrayList<>(numOfVertices);
            for (int i = 0; i < numOfVertices; i++) {
                adjaList.add(new ArrayList<>());
            }
        }

        public boolean containsEdge(int start, int end) {
            return adjaList.get(start).contains(end);
        }

        public void addEdge(int start, int end) {
            adjaList.get(start).add(end);
        }

        public int V() {
            return numberOfVertices;
        }

        public List<Integer> adj(int vertex) {
            return adjaList.get(vertex);
        }

    }

    static void dfs(DirectedGraph G) {
        int V = G.V();

        color = createArray(V, WHITE);
        pi = createArray(V, NIL);

        a = createArray(V, 0);
        b = createArray(V, 0);

        for (int u = 0; u < V; u++) {
            if (color[u] == WHITE) {
                dfs_visit(G, u);
            }
        }

    }

    static void dfs_visit(DirectedGraph G, int u) {
        time = time + 1;
        a[u] = time;
        color[u] = GRAY;

        List<Integer> adj_u = G.adj(u);
        for (int v : adj_u) {
            if (color[v] == WHITE) {
                pi[v] = u;
                dfs_visit(G, v);
            }
        }
        color[u] = BLACK;
        time = time + 1;
        b[u] = time;
    }

    static int[] createArray(int length, int initialValue) {
        int[] array = new int[length];

        for (int i = 0; i < length; i++) {
            array[i] = initialValue;
        }

        return array;
    }

    static DirectedGraph createGraphRandomly(int numOfNodes, int numOfEdges) {
        DirectedGraph g = new DirectedGraph(numOfNodes);

        Random random = new Random();   //To choose edges at random --> 1 (b.2)

        for (int i = 0; i < numOfEdges; ) {
            int start = random.nextInt(numOfNodes);
            int end = random.nextInt(numOfNodes);

            if (start == end || g.containsEdge(start, end)) {
                continue;
            }

            g.addEdge(start, end);

            i++;
        }

        return g;
    }


        public static void main(String[] args) {

            //Prompt the user to input the number of nodes and the number of edges. --> 1 (a)
            try (Scanner stdin = new Scanner(System.in)) {
                System.out.print("Please enter the number of nodes: ");
                int numOfNodes = stdin.nextInt();

                System.out.print("Please enter the number of edges: ");
                int numOfEdges = stdin.nextInt();

                //Create a random adjacency list --> 1(b)
                DirectedGraph graph = createGraphRandomly(numOfNodes, numOfEdges);

                long begin = System.nanoTime();

                //Compute DFS --> 1(c)
                dfs(graph);

                //Measure and display the running time in nanoseconds --> 1(d)
                long end = System.nanoTime();
                double time = (end - begin);

                System.out.printf("Running time: %.3f \n", time);
            }

        }
    }