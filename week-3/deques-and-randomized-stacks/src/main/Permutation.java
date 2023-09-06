import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        int k;
        if (args.length == 0) {
            k = 1;
        } else {
            k = Integer.parseInt(args[0]);
        }

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            q.enqueue(word);
        }

        while (k > 0) {
            String word = q.dequeue();
            System.out.println(word);
            k--;
        }
    }
}