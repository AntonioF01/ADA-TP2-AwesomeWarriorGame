import java.util.LinkedList;
import java.util.List;

public class AwesomeWarriorGame {

    private final String PAYS = "Pays";
    private final String FINAl_ENERGY_OUTPUT = "Full of energy";

    private final List<Edge>[] graph;

    private int initialChallenge;
    private int finalChallenge;
    private int initialEnergy;

    @SuppressWarnings("unchecked")
    public AwesomeWarriorGame(int challenges) {
        this.graph = new LinkedList[challenges];

        for (int i = 0; i < graph.length; i++)
            graph[i] = new LinkedList<>();
    }

    public void handleConnection(int finishedChallenge, String action, int energy, int newChallenge) {
        graph[finishedChallenge].add(new Edge(newChallenge, action.equals(PAYS) ? energy : -energy));
    }

    public void processFinalLine(int initialChallenge, int finalChallenge, int initialEnergy) {
        this.finalChallenge = finalChallenge;
        this.initialChallenge = initialChallenge;
        this.initialEnergy = initialEnergy;
    }

    private long bellmanFord(List<Edge>[] graph, int origin) {
        long[] length = new long[graph.length];
        int[] via = new int[graph.length];

        for (int node = 0; node < graph.length; node++)
            length[node] = Long.MAX_VALUE;

        length[origin] = 0;
        via[origin] = origin;
        boolean changes;

        for (int i = 1; i < graph.length; i++) {
            changes = updateLengths(graph, length, via);
            if (!changes)
                break;
        }
        return length[finalChallenge];
    }

    private boolean updateLengths(List<Edge>[] graph, long[] len, int[] via) {
        boolean changes = false;
        for (int firstNode = 0; firstNode < graph.length; firstNode++)
            for (Edge e : graph[firstNode]) {
                int secondNode = e.node;
                if (len[firstNode] < Integer.MAX_VALUE) {
                    long newLen = len[firstNode] + e.weight;
                    if (newLen < len[secondNode]) {
                        len[secondNode] = newLen;
                        via[secondNode] = firstNode;
                        changes = true;
                    }
                }
            }
        return changes;
    }

    public String solve() {
        long energyConsumed = this.bellmanFord(this.graph, this.initialChallenge);
        if (energyConsumed <= 0)
            return FINAl_ENERGY_OUTPUT;
        else {
            long finalEnergy = Math.max(initialEnergy - energyConsumed, 0);
            return String.valueOf(finalEnergy);
        }
    }

    private static class Edge {
        private final int node;
        private final int weight;

        public Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }
}
