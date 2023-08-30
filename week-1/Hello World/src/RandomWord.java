import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int i=1;
        String champion = "";

        while (!StdIn.isEmpty()){
            String word = StdIn.readString();
            double probability = (double) 1/i;
            if (StdRandom.bernoulli(probability)){
                champion = word;
            }
            i+=1;
        }
        StdOut.println(champion);

    }
}
