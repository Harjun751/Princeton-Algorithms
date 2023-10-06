import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class WordNet {
    private SAP ancestralPath;

    private Digraph graph;

    private BiST<Integer, String> table;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        table = new BiST<>();

        In in = new In(synsets);
        int id = 0;
        while (!in.isEmpty()) {
            String sysetDetails = in.readLine();
            String[] details = sysetDetails.split(",");
            id = Integer.parseInt(details[0]);
            String[] words = details[1].split(" ");
            table.insertValuesWithMaster(id, words, details[1]);
        }
        // construct a digraph with number of vertices
        // which is the highest ID in synset
        Digraph wordGraph = new Digraph(id + 1);

        In in2 = new In(hypernyms);
        while (!in2.isEmpty()) {
            String hypernymDetails = in2.readLine();
            String[] ids = hypernymDetails.split(",");
            int currID = Integer.parseInt(ids[0]);
            int index = 1;
            while (index < ids.length) {
                int endID = Integer.parseInt(ids[index]);
                wordGraph.addEdge(currID, endID);
                index += 1;
            }
        }
        this.graph = wordGraph;

        // The input to the constructor does not correspond to a rooted DAG.
        // DAG = Directed *ACYCLIC* graph
        int index = 0;
        DFS dfs = new DFS();
        int numOrigins = 0;
        while (index < graph.V()) {
            if (!dfs.checkAcyclic(index)) {
                throw new IllegalArgumentException();
            }
            if (graph.outdegree(index) == 0 && graph.indegree(index) > 0) {
                numOrigins += 1;
            }
            index++;
        }
        if (numOrigins != 1) {
            throw new IllegalArgumentException();
        }
        ancestralPath = new SAP(wordGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return table.valueIterator();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return table.valueContains(word);
    }

    private void checkNouns(String nounA, String nounB) {
        if (nounB == null || nounA == null) {
            throw new IllegalArgumentException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        ArrayList<Integer> idA = table.getKeysByValue(nounA);
        ArrayList<Integer> idB = table.getKeysByValue(nounB);
        return ancestralPath.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        ArrayList<Integer> idA = table.getKeysByValue(nounA);
        ArrayList<Integer> idB = table.getKeysByValue(nounB);

        int nounID = ancestralPath.ancestor(idA, idB);
        if (nounID == -1) {
            return null;
        }
        else {
            return table.getValueByKey(nounID);
        }
    }

    private void testBFS(int item) {
        Queue<Integer> search = new Queue<>();
        search.enqueue(item);
        boolean[] marked = new boolean[graph.V()];
        marked[item] = true;
        while (!search.isEmpty()) {
            int vertex = search.dequeue();
            for (int neighbour : this.graph.adj(vertex)) {
                if (!marked[neighbour]) {
                    search.enqueue(neighbour);
                    marked[neighbour] = true;
                    System.out.println(this.table.getValueByKey(neighbour) + " : " + neighbour);
                }
            }
        }
    }
    // NOUNS OPERATIONS -> NOUNS MAY EXIST MORE THAN ONCE
    // GET NOUN BY ID -> ARRAY
    // CHECK NOUN EXISTENCE ->
    // ITERATE THRU NOUNS
    // GET ID BY NOUN
    // SPACE O(N)
    // TIME O(N*logN)

    // do unit testing of this class
    public static void main(String[] args) {
    }

    // a generic bidirectional symbol table
    // that will be used for fast lookups
    private class BiST<T1 extends Comparable<T1>, T2 extends Comparable<T2>> {
        // id : string
        private ST<T1, T2> keyValueSet;
        // string : id
        private ST<T2, ArrayList<T1>> valueKeySet;

        public BiST() {
            keyValueSet = new ST<>();
            valueKeySet = new ST<>();
        }

        public void insertValuesWithMaster(T1 key, T2[] values, T2 master) {
            // insertion with two values - one being the greater, "master" value that goes into the key-val set
            for (T2 val : values) {
                ArrayList<T1> keys = getKeysByValue(val);
                if (keys == null) {
                    keys = new ArrayList<>();
                }
                keys.add(key);
                valueKeySet.put(val, keys);
            }
            keyValueSet.put(key, master);
        }

        public T2 getValueByKey(T1 key) {
            return keyValueSet.get(key);
        }

        public ArrayList<T1> getKeysByValue(T2 value) {
            return valueKeySet.get(value);
        }


        public Iterable<T2> valueIterator() {
            return valueKeySet.keys();
        }

        public boolean valueContains(T2 value) {
            return valueKeySet.contains(value);
        }
    }


    private class DFS {
        private boolean[] marked;

        public DFS() {
            marked = new boolean[graph.V()];
        }

        public boolean checkAcyclic(int v) {
            if (marked[v]) {
                return true;
            }
            else {
                marked[v] = true;
                return checkAcyclic(v, v);
            }
        }

        private boolean checkAcyclic(int v, int origin) {
            for (int neighbour : graph.adj(v)) {
                if (!marked[neighbour]) {
                    marked[neighbour] = true;
                    return checkAcyclic(neighbour, origin);
                }
                else if (neighbour == origin) {
                    return false;
                }
            }
            return true;
        }
    }
}