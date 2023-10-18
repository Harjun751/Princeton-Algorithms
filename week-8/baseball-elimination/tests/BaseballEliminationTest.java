import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.SET;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;

class BaseballEliminationTest {
    @Test
    void testCtor(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4.txt");
        SET<String> teams = new SET<>();
        teams.add("Atlanta");
        teams.add("New_York");
        teams.add("Philadelphia");
        teams.add("Montreal");
        for (String team : bbe.teams()){
            assertTrue(teams.contains(team));
            teams.remove(team);
        }
        assertTrue(teams.isEmpty());
    }
//    @Test
//    void testCreateNetwork(){
//        // note, hand-tested for Atl-Philly-NYC-Montreal case, particularly NYC
//        String filePath = new File("").getAbsolutePath();
//        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4.txt");
//
//        FlowNetwork network = bbe.createNetwork(2);
//        System.out.println(network.toString());
//    }
    @Test
    void testText4NYC(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4.txt");

        assertFalse(bbe.isEliminated("New_York"));
    }

    @Test
    void testEliminationText4(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4.txt");
        //        Atlanta is not eliminated
        assertFalse(bbe.isEliminated("Atlanta"));

        //        Philadelphia is eliminated by the subset R = { Atlanta New_York }
        assertTrue(bbe.isEliminated("Philadelphia"));
        SET<String> teams = new SET<>();
        teams.add("Atlanta");
        teams.add("New_York");
        for (String team : bbe.certificateOfElimination("Philadelphia")){
            assertTrue(teams.contains(team));
            teams.delete(team);
        }
        assertTrue(teams.isEmpty());

        //        New_York is not eliminated
        assertFalse(bbe.isEliminated("New_York"));

        //        Montreal is eliminated by the subset R = { Atlanta }
        assertTrue(bbe.isEliminated("Montreal"));
        teams.add("Atlanta");
        teams.add("Philadelphia");
        teams.add("New_York");
        for (String team : bbe.certificateOfElimination("Montreal")){
            assertTrue(teams.contains(team));
            teams.remove(team);
        }
        assertTrue(teams.isEmpty());
    }

    @Test
    void testEliminationText5(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams5.txt");
        //        New_York is not eliminated
        assertFalse(bbe.isEliminated("New_York"));
        //        Baltimore is not eliminated
        assertFalse(bbe.isEliminated("Baltimore"));
        //        Boston is not eliminated
        assertFalse(bbe.isEliminated("Boston"));
        //        Toronto is not eliminated
        assertFalse(bbe.isEliminated("Toronto"));

        //        Detroit is eliminated by the subset R = { New_York Baltimore Boston Toronto }
        assertTrue(bbe.isEliminated("Detroit"));
        SET<String> teams = new SET<>();
        teams.add("Baltimore");
        teams.add("Boston");
        teams.add("Toronto");
        teams.add("New_York");
        for (String team : bbe.certificateOfElimination("Detroit")){
            assertTrue(teams.contains(team));
            teams.delete(team);
        }
        assertTrue(teams.isEmpty());
    }

    @Test
    void testEliminationHairyPodder(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4b.txt");
        SET<String> teams = new SET<>();
        teams.add("Gryffindor");
        for (String team : bbe.certificateOfElimination("Hufflepuff")){
            assertTrue(teams.contains(team));
            teams.delete(team);
        }
        assertTrue(teams.isEmpty());
    }

    @Test
    void testEliminationText4_(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams4.txt");
        bbe.certificateOfElimination("Montreal");
    }
    @Test
    void testEliminationText12(){
        String filePath = new File("").getAbsolutePath();
        BaseballElimination bbe = new BaseballElimination(filePath+"/inputs/teams12.txt");
        bbe.certificateOfElimination("Japan");
    }
}