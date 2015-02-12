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
    private int target;

    //path variables
    private boolean pathsAreCurrent;
    DominoRun longest;
    DominoRun mostPoints;

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param edges The array of edges to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    DominoGraph(int maximumDouble, Domino edges[], int startDouble) {
        MAX_EDGE = maximumDouble;

        graph = new DominoVertex[maximumDouble + 1];

        //reserves the graph's memory.
        for (int i = 0; i <= maximumDouble; i++) {
            graph[i] = new DominoVertex(MAX_EDGE);
        }

        //initializes the graph.
        for (Domino d : edges) {
            addEdgePair(d.getVal1(), d.getVal2());
        }

        pathsAreCurrent = false;
        longest = new DominoRun();
        mostPoints = new DominoRun();
        target = startDouble;
    }

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param h The hand to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    DominoGraph(int maximumDouble, Hand h, int startDouble) {
        MAX_EDGE = maximumDouble;

        Domino edges[] = h.toArray();

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

        target = startDouble;
    }

    /**
     * Adds an edge pair to this graph.
     * @param v1 Vertex 1
     * @param v2 Vertex 2
     */
    private void addEdgePair(int v1, int v2) {
        try {
            graph[v1].addEdge(v2);
            graph[v2].addEdge(v1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new AssertionError("Out of bounds! Bad domino!");
        }

        pathsAreCurrent = false;
    }

    /**
     * Toggles an edge pair in this graph.
     * @param v1 Vertex 1
     * @param v2 Vertex 2
     */
    private void toggleEdgePair(int v1, int v2) {
        try {
            graph[v1].toggleEdge(v2);
            graph[v2].toggleEdge(v1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new AssertionError("Out of bounds! Bad domino!");
        }
    }

    /**
     * Deletes an edge pair from this graph.
     * @param v1 Vertex 1
     * @param v2 Vertex 2
     */
    private void deleteEdgePair(int v1, int v2) {
        try {
            if (graph[v1].hasEdge(v2)) {
                graph[v1].toggleEdge(v2);
                graph[v2].toggleEdge(v1);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new AssertionError("Out of bounds! Bad domino!");
        }
    }

    //TODO unfinished
    //WARNING: Assumes max double already played.
    //recalculates longest/most point path. Uses brute force.
    private void recalculatePaths() {
        pathsAreCurrent = true;

        DominoRun tempRun = new DominoRun();
        //Start edges must be played on the the start.
        boolean startEdges[] = graph[MAX_EDGE].dumpEdges();

        //We blank out the start edges.
        for (int i = 0; i <= MAX_EDGE; i++) {
            deleteEdgePair(MAX_EDGE, i);
        }

        //We need to start on the main domino, so we go through each one individually.
        if (target == MAX_EDGE) {
            for (int i = 0; i <= MAX_EDGE; i++) {
                if (startEdges[i]) {
                    target = i;
                    //TODO wrong here, fix
                    recalculatePaths();
                }
            }
        }
        //Normal case: we don't have to play off the main domino.
        else {

        }

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

    /**
     * Sets a new target for the graph.
     * @param targetVertex The new vertex to target.
     */
    public void setTarget(int targetVertex) {
        if (targetVertex != target) {
            if (targetVertex <= MAX_EDGE)
                target = targetVertex;
            else
                throw new AssertionError("Target domino must be <= MAX_EDGE.");

            recalculatePaths();
        }
    }

    /**
     * Dequeues the longest front, has to re-calculate for the most point path.
     * @return The previous front of the longest list.
     */
    public Domino dequeueLongest() {
        Domino retVal;

        retVal = longestPath().popFront();
        target = retVal.getVal2();
        recalculatePaths();

        return retVal;
    }

    /**
     * Dequeues the most point front, has to re-calculate for the longest path.
     * @return The previous front of the most point path.
     */
    public Domino dequeueMostPoints() {
        Domino retVal;

        retVal = mostPointPath().popFront();
        target = retVal.getVal2();
        recalculatePaths();

        return retVal;
    }
}
