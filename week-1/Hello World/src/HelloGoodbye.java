import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length>1) {
            StdOut.print("Hello " + args[0] + " and " + args[1] + "\n");
            StdOut.print("Goodbye " + args[1] + " and " + args[0] + "\n");
        }
    }
}
