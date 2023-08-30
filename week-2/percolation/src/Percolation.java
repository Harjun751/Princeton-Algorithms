import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF unionGrid;
    private final int gridSize;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        // Efficiency N*2? Idk about the .clone() operation.
        grid = new boolean[n][n];
        unionGrid = new WeightedQuickUnionUF(n*n+2);
        gridSize=n;

        boolean[] row = new boolean[n];
        for (int x=0; x<n;x++){
            row[x] = false;
        }
        while (n>0) {
            n--;
            grid[n] = row.clone();
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        row--;
        col--;
        if (row>gridSize || col>gridSize){
            throw new IllegalArgumentException();
        }
        else {
            grid[row][col] = true;
            try {
                // Union on above element IF IT IS OPEN
                if (row-1>=0 && grid[row-1][col]){
                    unionGrid.union((gridSize*row+col), (gridSize*(row-1)+col));
                }
                if (row+1<gridSize && grid[row+1][col]){
                    // Union on below element
                    unionGrid.union((gridSize*row+col), (gridSize*(row+1)+col));
                }
                if (col-1>=0 && grid[row][col-1]){
                    // Union on left element
                    unionGrid.union((gridSize*row+col), (gridSize*row+(col-1)));
                }
                if (col+1<gridSize && grid[row][col+1]){
                    // Union on right element
                    unionGrid.union((gridSize*row+col), (gridSize*row+(col+1)));
                }
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        row--;
        col--;
        if (row>gridSize || col>gridSize){
            throw new IllegalArgumentException();
        } else {
            return grid[row][col];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        return !isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        int openSites = 0;
        for (int row = 0; row<gridSize; row++){
            for (int col = 0; col<gridSize; col++){
                if (grid[row][col]){
                    openSites++;
                }
            }
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        // Create a psuedo top & bottom element that connects to all edge-level components
        // edge-level components = first N elements and last N elements
        for (int x=0;x<gridSize;x++){
            // union the top-level element to the top psuedo element
            unionGrid.union(gridSize*gridSize, x);
            // union the bottom-level element to the bottom pseudo element
            unionGrid.union((gridSize*gridSize)+1, (gridSize*gridSize)-1-x);
        }
        // Check if the top & bottom pseudo element connect
        return unionGrid.find(gridSize*gridSize) == unionGrid.find((gridSize*gridSize)+1);
    }

    // test client (optional)
    public static void main(String[] args){
        Percolation nGrid = new Percolation(5);
        nGrid.open(1,3);
        nGrid.open(2,3);
        nGrid.open(3,3);
        nGrid.open(4,3);
        nGrid.open(5,3);
        nGrid.open(5,5);

        System.out.println(nGrid.percolates());
    }
}
