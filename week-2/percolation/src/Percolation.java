import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.StdRandom;


public class Percolation {
    private final boolean[][] objectArray;
    private final WeightedQuickUnionUF idArray;
    private final int gridSize;
    private int openSites = 0;

    private int convertGridToIDIndex(int row, int col) {
        return gridSize * (row - 1) + col - 1;
    }

    private void setObjectTrue(int row, int col) {
        if (!objectArray[row - 1][col - 1]){
            this.openSites += 1;
        }
        objectArray[row - 1][col - 1] = true;
    }

    // Helper method to make code more readable
    private boolean getObject(int row, int col) {
        return objectArray[row - 1][col - 1];
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        // Create the original object array (n by n size)
        objectArray = new boolean[n][n];
        // Create the ID array for union find - includes a +2 for the top and bottom pseudoelements
        idArray = new WeightedQuickUnionUF(n * n + 2);
        // Initialize the size of the grid
        gridSize = n;

        // Create a row of booleans
        // Representing if the node is "open" or "closed"
        boolean[] row = new boolean[n];
        for (int x = 0; x < n; x++) {
            row[x] = false;
        }
        // Clone the rows to create the full grid
        // Starting from the bottom index, 4, til 0
        while (n > 0) {
            n--;
            objectArray[n] = row.clone();
        }

        // Set up the top and bottom pseudo-elements
        // Create a psuedo top & bottom element that connects to all edge-level components
        int topElementIndex = gridSize * gridSize;
        int bottomElementIndex = gridSize * gridSize + 1;

        // edge-level components = first N elements and last N elements
        // For loop occurs gridSize amount of times
        for (int x = 0; x < gridSize; x++) {
            // union the top-level element to the top psuedo element if it is open
            idArray.union(topElementIndex, x);
            // union the bottom-level element to the bottom pseudo element
            // Note: A little wonky but it's because the last index of a REAL
            // ID in the idArray is index N*N-1. It is then minused by whatever X is current, hence working backwards.
            idArray.union(bottomElementIndex, (gridSize * gridSize) - 1 - x);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // Check if sizes exceed the limits of the grid
        if (row > gridSize || col > gridSize || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }

        this.setObjectTrue(row, col);

        // Do some basic checks and union nearby objects if required
        try {
            int currentElementIndex = convertGridToIDIndex(row, col);

            // Union on above element if it is open
            if (row - 1 > 0 && this.getObject(row - 1, col)) {
                idArray.union(currentElementIndex, convertGridToIDIndex(row - 1, col));
            }
            // Union on below element
            if (row + 1 <= gridSize && this.getObject(row + 1, col)) {
                idArray.union(currentElementIndex, convertGridToIDIndex(row + 1, col));
            }
            // Union on left element
            if (col - 1 > 0 && this.getObject(row, col - 1)) {
                idArray.union(currentElementIndex, convertGridToIDIndex(row, col - 1));
            }
            // Union on right element
            if (col + 1 <= gridSize && this.getObject(row, col + 1)) {
                idArray.union(currentElementIndex, convertGridToIDIndex(row, col + 1));
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }

    public boolean isOpen(int row, int col) {
        // Check if row/col exceeds grid
        if (row > gridSize || col > gridSize || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }

        // return the object boolean
        return this.getObject(row, col);
    }

    public boolean isFull(int row, int col) {
        int topElementIndex = gridSize * gridSize;

        int objectID = this.convertGridToIDIndex(row, col);
        if (!this.isOpen(row, col)) {
            // Unopened sites cannot be full
            return false;
        }
        if (idArray.find(topElementIndex) == idArray.find(objectID)) {
            if (row==gridSize){
                try {
                    return isFull(row-1, col);
                } catch (IllegalArgumentException e) {

                }try {
                    return isFull(row, col-1);
                } catch (IllegalArgumentException e) {

                }try {
                    return isFull(row-1, col+1);
                } catch (IllegalArgumentException e) {

                }

            }
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        // Check if the top & bottom pseudoelement connect
        return idArray.find(gridSize * gridSize) == idArray.find((gridSize * gridSize) + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1,3);
        perc.open(2,3);
        perc.open(3,3);
        perc.open(3,1);
        perc.open(2,1);
        System.out.println(perc.isFull(3,1));
    }
}
