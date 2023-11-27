import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k;
        if (args.length == 0) {
            k = 3;
        } else {
            k = Integer.parseInt(args[0]);
        }
        if (k == 0) {
            return;
        }
        RandomizedQueue<String> q = new RandomizedQueue<>();


        ////////////////////////////////////////////////
        //           100 POINT IMPLEMENTATION         //
        ////////////////////////////////////////////////
//        if (args.length == 0) {
//            k = 1;
//        } else {
//            k = Integer.parseInt(args[0]);
//        }
//
//        while (!StdIn.isEmpty()) {
//            String word = StdIn.readString();
//            q.enqueue(word);
//        }
//
//        while (k > 0) {
//            String word = q.dequeue();
//            System.out.println(word);
//            k--;
//        }
        ////////////////////////////////////////////////
        ///////////////////// END //////////////////////
        ////////////////////////////////////////////////




        ////////////////////////////////////////////////
        // 95 POINT IMPLEMENTATION WITH BONUS POINTS //
        ////////////////////////////////////////////////
        //  This solution uses reservoir sampling. It maintains
        //  a randomized queue of size k throughout the whole
        //  execution, doing sampling in one pass of the stream.
        //  This uses less memory overall.

        //  The reason why this code isn't 100 points is that
        //  the sampling is not uniform - a problem I haven't
        //  been able to think of a solution to.
        int counter = 0;
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (counter >= k) {
                int r = StdRandom.uniformInt(0, counter);
                if (r < k - 1) {
                    // dequeue a random word
                    q.dequeue();
                    // enq this word
                    q.enqueue(word);
                }
            } else {
                q.enqueue(word);
            }
            counter++;
        }


        for (String str : q) {
            System.out.println(str);
        }
        ////////////////////////////////////////////////
        ///////////////////// END //////////////////////
        ////////////////////////////////////////////////
    }
}