import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
class WordNetTest {
    @Test
    void testWN_1() {
        WordNet net = new WordNet("inputs/synsets.txt", "inputs/hypernyms.txt");
        String nounA = "mouse";
        String nounB = "absorbent";

        assertEquals(8, net.distance(nounA, nounB));
    }

    @Test
    void testWN_2() {
        WordNet net = new WordNet("inputs/synsets.txt", "inputs/hypernyms.txt");
        String nounA = "foodstuff";
        String nounB = "dead_soul";

        assertEquals(7, net.distance(nounA, nounB));
    }

    @Test
    void testWN_fails() {
        WordNet net = new WordNet("inputs/synsets.txt", "inputs/hypernyms6InvalidCycle.txt");
        String nounA = "foodstuff";
        String nounB = "dead_soul";

        assertEquals(7, net.distance(nounA, nounB));
    }
}