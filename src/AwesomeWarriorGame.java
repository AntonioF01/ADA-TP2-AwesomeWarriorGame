import java.util.LinkedList;
import java.util.List;

public class AwesomeWarriorGame {

    private final String PAYS = "Pays";



    private final int challenges;
    private final int decisions;

    private int initialChallenge;
    private int finalChallenge;
    private int initialEnergy;

    private final List<Edge>[] graph;

    @SuppressWarnings("unchecked")
    public AwesomeWarriorGame(int challenges, int decisions) {
        this.graph = new LinkedList[challenges];
        this.challenges = challenges;
        this.decisions = decisions;
    }

    public void handleConnection(int finishedChallenge, String action, int energy, int newChallenge) {
        graph[finishedChallenge].add(new Edge(newChallenge, action.equals(PAYS) ? -energy : energy));
    }

    public void processFinalLine(int finalChallenge, int initialChallenge, int initialEnergy){
        this.finalChallenge = finalChallenge;
        this.initialChallenge = initialChallenge;
        this.initialEnergy = initialEnergy;
    }

    private static class Edge {
        private final int node;
        private final int weight;

        public Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }

        /*@Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof Edge)) return false;
            return ((Edge) other).node == this.node;
        }*/
    }


}
