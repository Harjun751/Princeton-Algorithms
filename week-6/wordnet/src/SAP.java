import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stack;

public class SAP {
    private Digraph graph;

    // FORMAT OF THE CACHE
    // Access using the integer index of the vertex in question
    // then, the ST<Integer, Integer[]> stores, for that vertex,
    // the ancestor and distance (Integer[]) for the QUERY VERTEX (Integer)
    private ST<Integer, ST<Integer, Integer[]>> cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
        this.cache = new ST<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ST<Integer, Integer[]> ans = cache.get(v);
        if (ans != null) {
            if (ans.contains(w)) {
                return ans.get(w)[0];
            }
        }
        else {
            ans = new ST<>();
        }
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w, cache);
        Integer[] answers = new Integer[] { search.getDistance(), search.getAncestor() };
        ST<Integer, Integer[]> alt = cache.get(w);

        if (alt == null) {
            alt = new ST<>();
        }
        ans.put(w, answers);
        alt.put(v, answers);
        cache.put(v, ans);
        cache.put(w, alt);
        return search.getDistance();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ST<Integer, Integer[]> ans = cache.get(v);
        if (ans != null) {
            if (ans.contains(w)) {
                return ans.get(w)[1];
            }
        }
        else {
            ans = new ST<>();
        }
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w, cache);
        Integer[] answers = new Integer[] { search.getDistance(), search.getAncestor() };
        ST<Integer, Integer[]> alt = cache.get(w);

        if (alt == null) {
            alt = new ST<>();
        }
        ans.put(w, answers);
        alt.put(v, answers);
        cache.put(v, ans);
        cache.put(w, alt);
        return search.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w, cache);
        return search.getDistance();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w, cache);
        return search.getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        Queue<Integer> v = new Queue<>();
        Queue<Integer> w = new Queue<>();
        v.enqueue(null);
        v.enqueue(-1);
        SAP sap = new SAP(G);
        sap.ancestor(null, null);
    }

    private class BreadthFirstSearch {

        private int ancestor = -1;

        private int distance = Integer.MAX_VALUE;

        private ST<Integer, ST<Integer, Integer[]>> cache;


        public BreadthFirstSearch(Digraph g, int source, int goal,
                                  ST<Integer, ST<Integer, Integer[]>> cache) {
            Stack<Integer> sourceIter = new Stack<>();
            Stack<Integer> goalIter = new Stack<>();
            sourceIter.push(source);
            goalIter.push(goal);
            this.cache = cache;
            // pass the call to BFS
            this.manager(g, sourceIter, goalIter);
        }

        public BreadthFirstSearch(Digraph g, Iterable<Integer> source, Iterable<Integer> goal,
                                  ST<Integer, ST<Integer, Integer[]>> cache) {
            // pass the call to BFS
            this.cache = cache;
            this.manager(g, source, goal);
        }

        private void manager(Digraph g, Iterable<Integer> source, Iterable<Integer> goal) {
            if (source == null || goal == null) {
                throw new IllegalArgumentException();
            }

            int[] sourceDistance = new int[g.V()];
            int[] goalDistance = new int[g.V()];

            boolean[] sourceMarked = new boolean[g.V()];
            boolean[] goalMarked = new boolean[g.V()];

            SET<Integer> srcSet = new SET<>();

            Queue<Integer> sourceSearch = new Queue<>();
            Queue<Integer> goalSearch = new Queue<>();
            int sourceUnreachableCount = 0;
            int goalUnreachableCount = 0;
            int goalCount = 0;

            for (Integer v : source) {
                if (v == null || v < 0) {
                    throw new IllegalArgumentException();
                }
                // check if source is reachable
                if ((g.indegree(v) == 0 && g.outdegree(v) == 0)) {
                    sourceUnreachableCount += 1;
                }
                else {
                    sourceSearch.enqueue(v);
                    sourceMarked[v] = true;
                }
                srcSet.add(v);
            }


            for (Integer v : goal) {
                if (v == null || v < 0) {
                    throw new IllegalArgumentException();
                }
                // check if the sources contain the exact same goal
                if (srcSet.contains(v)) {
                    // if source and goal are the same, we have found SAP.
                    this.distance = 0;
                    this.ancestor = v;
                    return;
                }

                // check if goal is reachable
                if ((g.indegree(v) == 0 && g.outdegree(v) == 0)) {
                    goalUnreachableCount += 1;
                }
                else {
                    // if reachable, add to search
                    goalSearch.enqueue(v);
                    goalMarked[v] = true;
                    for (Integer x : srcSet) {
                        // set cache
                        ST<Integer, Integer[]> ans = cache.get(v);
                        if (ans != null) {
                            Integer[] answer = ans.get(x);
                            if (answer != null) {
                                this.distance = answer[0];
                                this.ancestor = answer[1];
                            }
                        }
                    }
                }
                goalCount += 1;
            }

            if (goalUnreachableCount == goalCount || sourceUnreachableCount == srcSet.size()) {
                // if either SET of goals/sources are unreachable,
                // there is no path.
                this.distance = -1;
                this.ancestor = -1;
                return;
            }


            // start the actual lockstep-search
            while (!goalSearch.isEmpty() || !sourceSearch.isEmpty()) {
                if (!sourceSearch.isEmpty()) {
                    int sourceVertex = sourceSearch.dequeue();
                    for (int neighbour : graph.adj(sourceVertex)) {
                        if (!sourceMarked[neighbour]) {
                            sourceSearch.enqueue(neighbour);
                            sourceMarked[neighbour] = true;
                            sourceDistance[neighbour] = sourceDistance[sourceVertex] + 1;
                            if (sourceDistance[neighbour] > this.distance) {
                                // current search branch is longer than shortest distance found so far
                                // we don't need to search any further.
                                while (!sourceSearch.isEmpty()) {
                                    sourceSearch.dequeue();
                                }
                                break;
                            }

                            if (goalMarked[neighbour]) {
                                // we have found an overlapping mark.
                                // this is a possible SAP.
                                int newDist = sourceDistance[neighbour] + goalDistance[neighbour];
                                if (newDist < this.distance) {
                                    this.distance = newDist;
                                    this.ancestor = neighbour;
                                }
                            }
                        }
                    }
                }

                if (!goalSearch.isEmpty()) {
                    int goalVertex = goalSearch.dequeue();

                    for (int neighbour : graph.adj(goalVertex)) {
                        if (!goalMarked[neighbour]) {
                            goalSearch.enqueue(neighbour);
                            goalMarked[neighbour] = true;
                            goalDistance[neighbour] = goalDistance[goalVertex] + 1;
                            if (goalDistance[neighbour] > this.distance) {
                                // current search branch is longer than shortest distance found so far
                                // we don't need to search any further.
                                while (!goalSearch.isEmpty()) {
                                    goalSearch.dequeue();
                                }
                                break;
                            }
                            if (sourceMarked[neighbour]) {
                                // we have found an overlapping mark.
                                // this is a possible SAP.
                                int newDist = sourceDistance[neighbour] + goalDistance[neighbour];
                                if (newDist < this.distance) {
                                    this.distance = newDist;
                                    this.ancestor = neighbour;
                                }
                            }
                        }

                    }
                }
            }
            if (ancestor == -1) {
                distance = -1;
            }
        }

        public int getDistance() {
            return this.distance;
        }

        public int getAncestor() {
            return ancestor;
        }
    }


}
