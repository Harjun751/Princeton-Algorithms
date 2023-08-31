import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] percolationData;
    private final int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        t = trials;
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        percolationData = new double[trials];
        for (int x = 0; x < trials; x++) {
            Percolation test = new Percolation(n);
            percolationData[x] = (double) test.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationData);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationData);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean() - (1.96 * this.stddev()) / (Math.sqrt(t)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean() + (1.96 * this.stddev()) / (Math.sqrt(t)));
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        int nConverted = Integer.parseInt(args[0]);
        int trialsConverted = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(nConverted, trialsConverted);
        System.out.println("mean\t\t\t\t\t\t= " + stats.mean());
        System.out.println("stddev\t\t\t\t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval \t= [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
