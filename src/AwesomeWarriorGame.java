import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AwesomeWarriorGame {

    private final String PAYS = "Pays";
    private final String FINAl_ENERGY_OUTPUT = "Full of energy";

    private final Edge[] edges;
    private int numEdges;

    private final int numNodes;
    private int initialChallenge;
    private int finalChallenge;
    private int initialEnergy;


    @SuppressWarnings("unchecked")
    public AwesomeWarriorGame(int challenges, int decisions) {
        this.numNodes = challenges;
        this.edges = new Edge[decisions];
        numEdges = 0;
    }

    public void handleConnection(int finishedChallenge, String action, int energy, int newChallenge) {
        int weight = action.equals(PAYS) ? energy : -energy;
        edges[numEdges++] = new Edge(finishedChallenge, weight, newChallenge);
    }

    public void processFinalLine(int initialChallenge, int finalChallenge, int initialEnergy) {
        this.finalChallenge = finalChallenge;
        this.initialChallenge = initialChallenge;
        this.initialEnergy = initialEnergy;
    }

    private long bellmanFord(Edge[] edges, int origin) throws NegativeWeightCycleException {
        long[] length = new long[this.numNodes];
        for (int node = 0; node < this.numNodes; node++)
            length[node] = Long.MAX_VALUE;
        length[origin] = 0;

        boolean changes = false;

        Set<Integer> canReachFinal = new HashSet<>();
        canReachFinal.add(finalChallenge);

        for (int i = 1; i < this.numNodes; i++) {
            changes = updateLengths(edges, length, canReachFinal);
            if (!changes)
                break;
        }

        long[] prevLength = length.clone();
        if (changes && updateLengths(edges, length, canReachFinal))
            for (int i = 0; i < this.numNodes; i++)
                if (prevLength[i] != length[i] && canReachFinal.contains(i))
                    throw new NegativeWeightCycleException();

        return length[finalChallenge];
    }

    private boolean updateLengths(Edge[] edges, long[] len, Set<Integer> canReachFinal) {
        boolean changes = false;
        for (Edge e : edges) {
            if (canReachFinal.contains(e.secondNode))
                canReachFinal.add(e.firstNode);
            if (len[e.firstNode] < Integer.MAX_VALUE) {
                long newLen = len[e.firstNode] + e.weight;
                if (newLen < len[e.secondNode]) {
                    len[e.secondNode] = newLen;
                    changes = true;
                }
            }
        }
        return changes;
    }

    public String solve() {
        try {
            long energyConsumed = this.bellmanFord(this.edges, this.initialChallenge);
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
        private final int firstNode;
        private final int secondNode;
        private final int weight;

        public Edge(int firstNode, int weight, int secondNode) {
            this.firstNode = firstNode;
            this.weight = weight;
            this.secondNode = secondNode;
        }
    }

    private static class NegativeWeightCycleException extends Exception {
    }
}