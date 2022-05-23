import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AwesomeWarriorGame {

    private final String PAYS = "Pays";
    private final String FINAl_ENERGY_OUTPUT = "Full of energy";

    private final List<Edge>[] successors;
    private final Set<Integer> canReachFinal;

    private int initialChallenge;
    private int finalChallenge;
    private int initialEnergy;

    @SuppressWarnings("unchecked")
    public AwesomeWarriorGame(int challenges) {
        this.successors = new LinkedList[challenges];
        this.canReachFinal = new HashSet<>(challenges);

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
        this.canReachFinal.add(finalChallenge);
    }

    private long bellmanFord(List<Edge>[] graph, int origin) throws NegativeWeightCycleException {
        long[] length = new long[graph.length];
        int[] via = new int[graph.length];

        for (int node = 0; node < graph.length; node++)
            length[node] = Long.MAX_VALUE;

        length[origin] = 0;
        via[origin] = origin;
        boolean changes = false;

        for (int i = 1; i < graph.length; i++) {
            changes = updateLengths(graph, length, via);
            if (!changes)
                break;
        }
        long[] aux = length.clone();
        if (changes && updateLengths(graph, length, via)) {
            for (int i = 0; i < aux.length; i++) {
                if (aux[i] != length[i] && canReachFinal.contains(i))
                    throw new NegativeWeightCycleException();
            }
        }
        return length[finalChallenge];
    }

    private boolean updateLengths(List<Edge>[] graph, long[] len, int[] via) {
        boolean changes = false;
        for (int firstNode = 0; firstNode < graph.length; firstNode++)
            for (Edge e : graph[firstNode]) {
                int secondNode = e.node;
                if (canReachFinal.contains(secondNode))
                    canReachFinal.add(firstNode);
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