public class Outcast {
    private WordNet net;

    public Outcast(WordNet wordnet) {
        this.net = wordnet;
    }

    public String outcast(String[] nouns) {
        int index = 0;

        int maxDistance = -1;
        String champion = null;

        while (index < nouns.length) {
            int tDistance = 0;
            for (String str2 : nouns) {
                if (!str2.equals(nouns[index])) {
                    tDistance += net.distance(nouns[index], str2);
                }
            }
            if (tDistance > maxDistance) {
                maxDistance = tDistance;
                champion = nouns[index];
            }
            index++;
        }

        return champion;
    }

    public static void main(String[] args) {
        // WordNet wordnet = new WordNet(args[0], args[1]);
        // Outcast outcast = new Outcast(wordnet);
        // for (int t = 2; t < args.length; t++) {
        //     In in = new In(args[t]);
        //     String[] nouns = in.readAllStrings();
        //     StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        // }
    }
}
