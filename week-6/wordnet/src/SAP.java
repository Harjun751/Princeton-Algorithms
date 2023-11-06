import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stack;

public class SAP {
    private Digraph graph;

    private Cache cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
        this.cache = new Cache();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // attempt to get answer from cache
        Integer ans = cache.getDistance(v, w);
        if (ans != null) {
            return ans;
        }

        // if not, create BFS object
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w);

        // insert item into cache
        cache.insert(v, w, search.getDistance(), search.getAncestor());

        return search.getDistance();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // attempt to get answer from cache
        Integer ans = cache.getAncestor(v, w);
        if (ans != null) {
            return ans;
        }

        // if not, create BFS object
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w);

        // insert into cache
        cache.insert(v, w, search.getDistance(), search.getAncestor());

        return search.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w);
        return search.getDistance();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstSearch search = new BreadthFirstSearch(graph, v, w);
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
        // sap.ancestor(null, null);
    }

    private class Cache {
        // CACHE DATA STRUCTURE
        // Source Vertex : Integer : Entry
        // Each vertex contains a symbol table of data concerning other vertexes
        private ST<Integer, ST<Integer, Entry>> cache;

        public Cache() {
            this.cache = new ST<>();
        }

        public Integer getDistance(int sourceVertex, int queryVertex) {
            ST<Integer, Entry> st = cache.get(sourceVertex);
            if (st != null) {
                Entry ent = st.get(queryVertex);
                if (ent != null) {
                    return ent.getDistance();
                }
            }
            return null;
        }

        public Integer getAncestor(int sourceVertex, int queryVertex) {
            ST<Integer, Entry> st = cache.get(sourceVertex);
            if (st != null) {
                Entry ent = st.get(queryVertex);
                if (ent != null) {
                    return ent.getAncestor();
                }
            }
            return null;
        }

        public boolean contains(int sourceVertex, int queryVertex) {
            return cache.get(sourceVertex).get(queryVertex) != null;
        }

        public void insert(int sourceVertex, int queryVertex, int distance, int ancestor) {
            Entry ent = new Entry(distance, ancestor);

            ST<Integer, Entry> sourceRecords = cache.get(sourceVertex);
            if (sourceRecords == null) {
                sourceRecords = new ST<>();
                cache.put(sourceVertex, sourceRecords);
            }
            sourceRecords.put(queryVertex, ent);

            ST<Integer, Entry> queryRecords = cache.get(queryVertex);
            if (queryRecords == null) {
                queryRecords = new ST<>();
                cache.put(queryVertex, queryRecords);
            }
            queryRecords.put(sourceVertex, ent);
        }

        private class Entry {
            private int ancestor;
            private int distance;

            public Entry(int distance, int ancestor) {
                this.ancestor = ancestor;
                this.distance = distance;
            }

            public int getAncestor() {
                return ancestor;
            }

            public int getDistance() {
                return distance;
            }
        }
    }

    private class BreadthFirstSearch {

        private int ancestor = -1;

        private int distance = Integer.MAX_VALUE;

        public BreadthFirstSearch(Digraph g, int source, int goal) {
            Stack<Integer> sourceIter = new Stack<>();
            Stack<Integer> goalIter = new Stack<>();
            sourceIter.push(source);
            goalIter.push(goal);
            // pass the call to BFS
            this.manager(g, sourceIter, goalIter);
        }

        public BreadthFirstSearch(Digraph g, Iterable<Integer> source, Iterable<Integer> goal) {
            // pass the call to BFS
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

            // Preprocess: Check if vertexes are reachable
            // if they are, add it to the source set
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

            // Same with goal
            // check if they are reachable
            // or if there are duplicates
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
                    // Check if there is a cached answer for this vertex
                    // is this required...
                    for (Integer x : srcSet) {
                        Integer anc = cache.getAncestor(v, x);
                        Integer dist = cache.getDistance(v, x);
                        if (anc != null && dist != null) {
                            this.ancestor = anc;
                            this.distance = dist;
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
                    // check every adjacent vertex for traversable area
                    for (int neighbour : graph.adj(sourceVertex)) {
                        if (!sourceMarked[neighbour]) {
                            // add it to the search, set distance
                            sourceSearch.enqueue(neighbour);
                            sourceMarked[neighbour] = true;
                            sourceDistance[neighbour] = sourceDistance[sourceVertex] + 1;

                            // Early termination
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
