import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AwesomeWarriorGame {

    private final String PAYS = "Pays";
    private final String FINAl_ENERGY_OUTPUT = "Full of energy";

    private final List<Edge>[] successors;

    private final int numNodes;
    private int initialChallenge;
    private int finalChallenge;
    private int initialEnergy;

    @SuppressWarnings("unchecked")
    public AwesomeWarriorGame(int challenges) {
        this.numNodes = challenges;
        this.successors = new LinkedList[challenges];

        for (int i = 0; i < challenges; i++)
            successors[i] = new LinkedList<>();
    }

    public void handleConnection(int finishedChallenge, String action, int energy, int newChallenge) {
        int weight = action.equals(PAYS) ? energy : -energy;
        successors[finishedChallenge].add(new Edge(newChallenge, weight));
    }

    public void processFinalLine(int initialChallenge, int finalChallenge, int initialEnergy) {
        this.finalChallenge = finalChallenge;
        this.initialChallenge = initialChallenge;
        this.initialEnergy = initialEnergy;
    }

    private long bellmanFord(List<Edge>[] graph, int origin) throws NegativeWeightCycleException {
        long[] length = new long[this.numNodes];
        boolean changes = false;
        Set<Integer> canReachFinal = new HashSet<>();

        for (int node = 0; node < this.numNodes; node++)
            length[node] = Long.MAX_VALUE;

        length[origin] = 0;
        canReachFinal.add(finalChallenge);

        for (int i = 1; i < this.numNodes; i++) {
            changes = updateLengths(graph, length, canReachFinal);
            if (!changes)
                break;
        }

        long[] prevLength = length.clone();
        if (changes && updateLengths(graph, length, canReachFinal))
            for (int i = 0; i < this.numNodes; i++)
                if (prevLength[i] != length[i] && canReachFinal.contains(i))
                    throw new NegativeWeightCycleException();

        return length[finalChallenge];
    }

    private boolean updateLengths(List<Edge>[] graph, long[] len, Set<Integer> canReachFinal) {
        boolean changes = false;
        for (int firstNode = 0; firstNode < this.numNodes; firstNode++)
            for (Edge e : graph[firstNode]) {
                int secondNode = e.node;
                if (canReachFinal.contains(secondNode))
                    canReachFinal.add(firstNode);
                if (len[firstNode] < Integer.MAX_VALUE) {
                    long newLen = len[firstNode] + e.weight;
                    if (newLen < len[secondNode]) {
                        len[secondNode] = newLen;
                        changes = true;
                    }
                }
            }
        return changes;
    }

    public String solve() {
        try {
            long energyConsumed = this.bellmanFord(this.successors, this.initialChallenge);
            if (energyConsumed <= 0)
                return FINAl_ENERGY_OUTPUT;
            else {
                long finalEnergy = Math.max(initialEnergy - energyConsumed, 0);
                return String.valueOf(finalEnergy);
            }
        } catch (NegativeWeightCycleException e) {
            return FINAl_ENERGY_OUTPUT;
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

    private static class NegativeWeightCycleException extends Exception {
    }
}