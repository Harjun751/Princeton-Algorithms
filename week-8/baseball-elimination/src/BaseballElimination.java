import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class BaseballElimination {
    private final String[] teams;
    private final ST<String, Integer> teamLookup;
    private final int[] wins;
    private final int[] loss;
    private final int[] left;
    private final int[][] games;
    private final int teamCount;

    private final int amountVertices;

    private final int sourceIndex;
    private final int sinkIndex;

    private SET<String> defaultWin;

    public BaseballElimination(String filename) {
        In file = new In(filename);
        teamCount = file.readInt();
        teams = new String[teamCount];
        teamLookup = new ST<>();
        wins = new int[teamCount];
        loss = new int[teamCount];
        left = new int[teamCount];
        games = new int[teamCount][teamCount];
        defaultWin = new SET<>();
        // amount of Vertices in the flow network
        // sink + source (2)    +
        // nTeams - 1           +
        // game vertices -> Each pair of teams other than team in question
        // we use the summation formula to get the amount of unique teams playing
        // against each other.
        amountVertices = 2 + teamCount + ((teamCount - 2) * (teamCount - 1)) / 2;
        // set the source and sink index to be used
        sourceIndex = amountVertices - 2;
        sinkIndex = amountVertices - 1;

        int i = 0;
        while (i < teamCount) {
            teams[i] = file.readString();
            teamLookup.put(teams[i], i);
            wins[i] = file.readInt();
            loss[i] = file.readInt();
            left[i] = file.readInt();

            int x = 0;

            while (x < teamCount) {
                int gamesLeft = file.readInt();
                games[i][x] = gamesLeft;
                x++;
            }
            i += 1;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamCount;
    }

    // all teams
    public Iterable<String> teams() {
        return () -> Arrays.stream(teams).iterator();
    }

    // number of wins for given team
    public int wins(String team) {
        int index = getTeamIndex(team);
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team) {
        int index = getTeamIndex(team);
        return loss[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int index = getTeamIndex(team);
        return left[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int i1 = getTeamIndex(team1);
        int i2 = getTeamIndex(team2);
        return games[i1][i2];
    }

    private int getTeamIndex(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        Integer index = teamLookup.get(team);
        if (index == null) {
            throw new IllegalArgumentException();
        }
        return index;
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        // Step 1: Check trivial elimination
        int index = getTeamIndex(team);
        if (checkTrivialElim(index)) {
            return true;
        }
        // Step 2: Create flow network
        FlowNetwork network = createNetwork(index);
        // create a fuk object with the source, sink
        FordFulkerson fuk = new FordFulkerson(network, sourceIndex, sinkIndex);
//        boolean isFull = true;
        // if edges from source not full, this team is eliminated
        double capacity = 0;
        for (FlowEdge e : network.adj(sourceIndex)) {
            capacity += e.capacity();
        }
        double maxFlow = fuk.value();
        // if full, it is not eliminated
        if (maxFlow < capacity) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkTrivialElim(int index) {
        int maxGamesWinnable = wins[index] + left[index];
        for (int i = 0; i < teamCount; i++) {
            // if win for any particular team is MORE THAN
            // max games winnable, team is eliminated
            if (wins[i] > maxGamesWinnable) {
                return true;
            }
        }
        return false;
    }

    private FlowNetwork createNetwork(int index) {
        int maxGamesWinnable = wins[index] + left[index];
        FlowNetwork network = new FlowNetwork(amountVertices);
        // create team -> sink
        for (int i = 0; i < teamCount; i++) {
            if (i == index) {
                continue;
            }
            // create the right side of the flow network
            // from team -> sink, with capacity of w[x]+r[x] - w[i]
            // prevent negative values in edge capacity
            int edgeCapacity = maxGamesWinnable - wins[i];
            if (edgeCapacity < 0) {
                // auto win, so no capacity needed - set to 0
                defaultWin.add(teams[i]);
                edgeCapacity = 0;
            }
            network.addEdge(new FlowEdge(i, sinkIndex, edgeCapacity));
        }

        ST<Integer, SET<Integer>> gameSet = new ST<>();
        // create source -> game vertex
        // as well as game vertex -> team
        int edgeIndex = teamCount;
        int t1 = 0;
        while (t1 < teamCount) {
            if (t1 == index) {
                // skip iteration
                t1++;
                continue;
            }
            int t2 = 0;
            while (t2 < teamCount) {
                if (t2 != t1 && t2 != index) {
                    // create flow edge of capacity of amount of games between these 2 parties
                    // but first, check if the game has already been added
                    if (!setContains(t1, t2, gameSet)) {
                        network.addEdge(new FlowEdge(sourceIndex, edgeIndex, games[t1][t2]));
                        // then, add an inf edge between the game vertex and the teams involved
                        network.addEdge(new FlowEdge(edgeIndex, t1, Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(edgeIndex, t2, Double.POSITIVE_INFINITY));
                        // add to set for lookups
                        addToSet(t1, t2, gameSet);
                        edgeIndex++;
                        if (edgeIndex > sourceIndex) {
                            break;
                        }
                    }
                }
                t2++;
            }
            t1++;
        }
        return network;
    }

    private void addToSet(int t1, int t2, ST<Integer, SET<Integer>> gameSet) {
        SET<Integer> set = gameSet.get(t1);
        if (set == null) {
            set = new SET<>();
            gameSet.put(t1, set);
        }
        set.add(t2);

        SET<Integer> set2 = gameSet.get(t2);
        if (set2 == null) {
            set2 = new SET<>();
            gameSet.put(t2, set2);
        }
        set2.add(t1);
    }

    private boolean setContains(int t1, int t2, ST<Integer, SET<Integer>> gameSet) {
        SET<Integer> set = gameSet.get(t1);
        if (set == null) {
            return false;
        } else {
            return set.contains(t2);
        }
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) {
            return null;
        }
        Queue<String> eliminatingTeams = new Queue<>();
        int index = getTeamIndex(team);
        // create a network
        FlowNetwork network = createNetwork(index);
        FordFulkersonCustom fuk = new FordFulkersonCustom(network);

        // get mincut teams
        for (int i = 0; i < teamCount; i++) {
            if (fuk.inCut(i)) {
                // add to mincut list
                // default win contains the list of teams
                // that won by default
                // ensure no overlap by only adding new teams
                if (!defaultWin.contains(teams[i])){
                    eliminatingTeams.enqueue(teams[i]);
                }
            }
        }
        // then, add the defaults
        for (String t : defaultWin){
            eliminatingTeams.enqueue(t);
        }
        return eliminatingTeams;
    }


    private class FordFulkersonCustom{
        private FlowNetwork network;
        private double limitingFactor;
        Integer[] pathTo;

        SET<Integer> minCutList;

        private double maxFlow;
        public FordFulkersonCustom(FlowNetwork n){
            // made this to get a deeper understanding of FF
            this.network = n;
            this.maxFlow = 0;
            this.minCutList = new SET<>();

            while (findAugmentingPath()){
                // find an augmenting path
                int prevIndex = -1;
                int v = sinkIndex;
                // Stops after reaching source vertex
                while (v!=-1){
                    if (prevIndex!=-1){
                        for (FlowEdge adj : network.adj(v)){
                            // find the flowedge where it s V pointing to the previous Index
                            // e.g.     v -> prevIndex
                            // we're using the previous index as it is reverse-traversing the path
                            if (adj.other(v) == prevIndex){
                                // add the limiting factor (i.e. the minimum capacity of every edge along this path)
                                // to the residual flow
                                adj.addResidualFlowTo(prevIndex, this.limitingFactor);
                                break;
                            }
                        }
                    }
                    prevIndex = v;
                    v = pathTo[v];
                }
                maxFlow += this.limitingFactor;
            }
            // get the minimum cut of the graph
            getMincut();
        }

        public boolean inCut(int v){
            return minCutList.contains(v);
        }

        private void getMincut(){
            int v = sourceIndex;
            boolean[] marked = new boolean[amountVertices];
            Queue<Integer> searchQ = new Queue<>();
            searchQ.enqueue(v);
            marked[v] = true;

            // minimunm cut capacity is the max flow
            // TODO: sort this out
            double cutCapacity = 0;
            while(!searchQ.isEmpty() && cutCapacity!=maxFlow){
                v = searchQ.dequeue();
                for (FlowEdge e : network.adj(v)){
                    int forwardV = e.other(v);
                    // if edge is not full, add it to the cut
                    if (e.residualCapacityTo(forwardV)!=0){
                        minCutList.add(v);
                        if (!marked[forwardV]){
                            searchQ.enqueue(forwardV);
                            marked[forwardV] = true;
                            cutCapacity+=e.residualCapacityTo(forwardV);
                        }
                    }
                }
            }
        }

        private boolean findAugmentingPath(){
            // check termination criteria -> all sources pointing to sink index are at residual capacity 0
            boolean terminate = true;
            for (FlowEdge e : network.adj(8)){
                if (e.residualCapacityTo(8) != 0){
                    terminate = false;
                }
            }
            if (terminate){
                return false;
            }

            Queue<Integer> searchQueue = new Queue<>();
            boolean[] marked = new boolean[amountVertices];
            pathTo = new Integer[amountVertices];
            pathTo[sourceIndex] = -1;
            marked[sourceIndex] = true;
            searchQueue.enqueue(sourceIndex);

            while (!searchQueue.isEmpty()){
                int v = searchQueue.dequeue();
                for (FlowEdge e : network.adj(v)){
                    int forward = e.other(v);
                    // find the residual capacity in the direction opposite from v.
                    // i.e. to say, the direction heading towards the sink
                    double capacity = e.residualCapacityTo(forward);
                    // if capacity of link is 0, it is not used in path
                    if (capacity > 0 && !marked[forward]){
                        pathTo[forward] = v;
                        marked[forward] = true;
                        searchQueue.enqueue(forward);
                    }
                }
            }

            // After the BFS, get the augmenting path
            if (pathTo[sinkIndex]==null){
                return false;
            }
            getLimitingValue();
            return true;
        }

        private void getLimitingValue(){
            int v = sinkIndex;
            int prevIndex = -1;
            double limitingValue = Double.POSITIVE_INFINITY;
            while (pathTo[v]!=-1){
                for (FlowEdge adj : network.adj(v)){
                    if(adj.other(v)==pathTo[v]){
                        limitingValue = Math.min(adj.residualCapacityTo(v), limitingValue);
                    }
                }
                v = pathTo[v];
            }
            this.limitingFactor = limitingValue;
        }


    }


    public static void main(String[] args) {
//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }
}
