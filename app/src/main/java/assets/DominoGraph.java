package assets;

import java.util.List;

/**
 * Created by Jacob on 2/11/2015.
 * A Domino graph, made of DominoVertexes.
 * Contains path processing options too.
 * TODO add path processing options.
 */
public class DominoGraph {
    private final int MAX_EDGE;
    private DominoVertex graph[];

    //path variables
    private boolean pathsAreCurrent;
    DominoRun longest;
    DominoRun mostPoints;

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param edges The array of edges to use to initialize the graph.
     */
    DominoGraph(int maximumDouble, Domino edges[]) {
        MAX_EDGE = maximumDouble;

        graph = new DominoVertex[maximumDouble + 1];

        //reserves the graph's memory.
        for (int i = 0; i <= maximumDouble; i++) {
            graph[i] = new DominoVertex(MAX_EDGE);
        }

        //initializes the graph.
        for (Domino d : edges) {
            try {
                graph[d.getVal1()].addEdge(d.getVal2());
                graph[d.getVal2()].addEdge(d.getVal1());
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new AssertionError("Out of bounds! Bad domino!");
            }
        }

        pathsAreCurrent = false;
        longest = new DominoRun();
        mostPoints = new DominoRun();
    }

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param h The hand to use to initialize the graph.
     */
    DominoGraph(int maximumDouble, Hand h) {
        MAX_EDGE = maximumDouble;

        //TODO make sure this works!
        Domino edges [] = h.toArray();

        graph = new DominoVertex[maximumDouble + 1];

        //reserves the graph's memory.
        for (int i = 0; i <= maximumDouble; i++) {
            graph[i] = new DominoVertex(MAX_EDGE);
        }

        //initializes the graph.
        for (Domino d : edges) {
            try {
                graph[d.getVal1()].addEdge(d.getVal2());
                graph[d.getVal2()].addEdge(d.getVal1());
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new AssertionError("Out of bounds! Bad domino!");
            }
        }
    }

    //recalculates longest/most point path.
    private void recalculatePaths() {
        pathsAreCurrent = true;

        DominoRun tempRun = new DominoRun();

        //TODO fill in brute force method here, really simple.
    }

    public DominoRun longestPath() {
        if (!pathsAreCurrent)
            recalculatePaths();

        return longest;
    }

    public DominoRun mostPointPath() {
        if (!pathsAreCurrent)
            recalculatePaths();

        return mostPoints;
    }

}
