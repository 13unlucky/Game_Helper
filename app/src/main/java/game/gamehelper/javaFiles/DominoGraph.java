package game.gamehelper.javaFiles;

import java.util.LinkedList;

/**
 * Created by Jacob on 2/11/2015.
 * A Domino graph, made of DominoVertexes.
 * Contains path processing options too.
 * TODO reduce brute force
 */
public class DominoGraph {
    private final int MAX_EDGE;
    private DominoVertex graph[];
    private int target;

    //debug/stats variables
    private int totalEdgeNum;
    private int repeatPointsFound;
    private int repeatLensFound;
    private int pathsFound;

    //path variables
    private boolean pathsAreCurrent;
    private DominoRun longest;
    private DominoRun mostPoints;

    private LinkedList<DominoRun> mostPointRuns;
    private LinkedList<DominoRun> longestRuns;
    private DominoRun currentRun;

    private int numVisited;

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param h The hand to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    DominoGraph(Hand h, int startDouble) {
        this (h.getLargestDouble(), h.toArray(), h.getLargestDouble());
    }

    /**
     * Generates a DominoGraph from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param edges The array of edges to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    DominoGraph(int maximumDouble, Domino edges[], int startDouble) {
        MAX_EDGE = maximumDouble;

        graph = new DominoVertex[MAX_EDGE + 1];
        totalEdgeNum = 0;

        //debug variables
        repeatLensFound = 0;
        repeatPointsFound = 0;
        numVisited = 0;
        pathsFound = 0;

        //reserves the graph's memory.
        for (int i = 0; i <= MAX_EDGE; i++) {
            graph[i] = new DominoVertex(MAX_EDGE);
        }

        //initializes the graph.
        for (Domino d : edges) {
            addEdgePair(d.getVal1(), d.getVal2());
            totalEdgeNum++;
        }

        pathsAreCurrent = false;
        longest = new DominoRun();
        mostPoints = new DominoRun();
        target = startDouble;
        mostPointRuns = new LinkedList<DominoRun>();
        longestRuns = new LinkedList<DominoRun>();
        currentRun = new DominoRun();
    }

    /**
     * Adds another domino to this graph.
     * @param d The domino to add.
     */
    public void addDomino(Domino d) {
        if (d.getVal1() > MAX_EDGE || d.getVal2() > MAX_EDGE)
            throw new AssertionError("New domino is too large.");

        //re-set paths if not here already
        if (!hasEdge(d.getVal1(), d.getVal2())) {
            pathsAreCurrent = false;
            totalEdgeNum++;
        }

        addEdgePair(d.getVal1(), d.getVal2());
    }

    /**
     * Removes a domino in this graph.
     * Will only throw exceptions when the domino is larger than the maximum domino.
     * @param d The domino to remove.
     */
    public void removeDomino(Domino d) {
        if (d.getVal1() > MAX_EDGE || d.getVal2() > MAX_EDGE)
            throw new AssertionError("Tried to delete domino larger than max domino.");

        //re-set paths if not here already
        if (hasEdge(d.getVal1(), d.getVal2())) {
            pathsAreCurrent = false;
            totalEdgeNum--;
        }

        removeEdgePair(d.getVal1(), d.getVal2());
    }

    //recalculates longest/most point path. Uses brute force.
    private void recalculatePaths() {
        currentRun = new DominoRun();
        mostPoints = new DominoRun();
        longest = new DominoRun();

        numVisited = 0;
        repeatLensFound = 0;
        repeatPointsFound = 0;
        pathsFound = 0;

        //We need to play the max double first! Will only show the max double.
        if (graph[MAX_EDGE].hasEdge(MAX_EDGE)) {
            currentRun.addDomino(new Domino(MAX_EDGE, MAX_EDGE));

            longest = currentRun.deepCopy();
            mostPoints = currentRun.deepCopy();

            pathsAreCurrent = true;
            target = MAX_EDGE;
            longestRuns.clear();
            mostPointRuns.clear();
            return;
        }

        //we've already played the max double, try to build off the current target.
        mostPointRuns.clear();
        longestRuns.clear();
        currentRun.clear();

        //Start edges must be played on the the start.
        boolean startEdges[] = graph[MAX_EDGE].dumpEdges();

        //We blank out the start edges.
        for (int i = MAX_EDGE; i >= 0; i--) {
            removeEdgePair(MAX_EDGE, i);
        }

        //We need to start on the main domino, so we go through each possible lead-off individually.
        //Remember, edges that map to the main domino must be played on the main domino.
        if (target == MAX_EDGE) {
            for (int i = 0; i <= MAX_EDGE; i++) {
                if (startEdges[i]) {
                    currentRun.clear();
                    addEdgePair(i, MAX_EDGE);
                    traverse(MAX_EDGE);
                    removeEdgePair(i, MAX_EDGE);
                }
            }
        }
        //Normal case: we don't have to play off the main domino.
        else {
            currentRun.clear();
            traverse(target);
        }

        //Re-add the edges to the main domino at the end.
        for (int i = 0; i <= MAX_EDGE; i++) {
            if (startEdges[i])
                addEdgePair(MAX_EDGE, i);
        }

        //simple heuristic to remove copy runs. Still will need more processing by a ProcessRun class.
        //TODO copy this functionality to a more-functional ProcessRun class
        for (DominoRun run : mostPointRuns) {
            if (run.isShorterThan(mostPoints))
                mostPoints = run.deepCopy();
        }
        for (DominoRun run : longestRuns) {
            if (run.hasMorePointsThan(longest))
                longest = run.deepCopy();
        }

        System.out.println("last repeat lens found: " + repeatLensFound);
        System.out.println("last repeat points found: " + repeatPointsFound);
        System.out.println("total vertexs visited: " + numVisited);
        System.out.println("total paths found: " + pathsFound);

        pathsAreCurrent = true;
        longestRuns.clear();
        mostPointRuns.clear();
    }

    //Does a pseudo-DFS of the graph.
    //is O(e^n), where n is the number of dominoes in the graph.
    //TODO sorta fixed. works with up to 27 dominoes.
    private boolean traverse(int startVertex) {
        numVisited++;
        //the current run has ended! Calculate runs!
        if (graph[startVertex].getEdgeNum() == 0) {
            pathsFound++;
            //this run isn't useful in terms of points
            if (mostPoints.hasMorePointsThan(currentRun)) {
                ;
            }
            //this run is so useful in terms of points, we have to get rid of the other runs.
            else if (currentRun.hasMorePointsThan(mostPoints)) {
                mostPointRuns.clear();
                mostPointRuns.add(currentRun.deepCopy());
                mostPoints = currentRun.deepCopy();
                System.out.println("more points: " + repeatPointsFound);
                repeatPointsFound = 0;
            }
            //this run has the same point value as the other runs.
            else {
                //We only want to get rid of the other run if this one is shorter.
                // If it's shorter, it's getting rid of points faster (on average).
                if (currentRun.isShorterThan(mostPoints)) {
                    mostPointRuns.clear();
                    mostPointRuns.add(currentRun.deepCopy());
                    mostPoints = currentRun.deepCopy();
                    System.out.println("more points: " + repeatPointsFound);
                    repeatPointsFound = 0;
                }
                repeatPointsFound++;
                //mostPointRuns.add(currentRun.deepCopy()); // removed because of memory problems.
            }

            //this run isn't useful in terms of length
            if (longest.isLongerThan(currentRun)) {
                ;
            }
            //this run is so useful in terms of length, we have to get rid of the other runs.
            else if (currentRun.isLongerThan(longest)) {
                longestRuns.clear();
                longestRuns.add(currentRun.deepCopy());
                longest = currentRun.deepCopy();
                if (longest.getLength() == totalEdgeNum)
                    return true;
                System.out.println("longer run: " + repeatLensFound);
                repeatLensFound = 0;
            }
            //this run has the same length as the other runs.
            else {
                //We want to get rid of the other run if this one is worth more points.
                // If it's worth more points, this one is getting rid of the points faster (on average).
                if (currentRun.hasMorePointsThan(longest)) {
                    longestRuns.clear();
                    longestRuns.add(currentRun.deepCopy());
                    longest = currentRun.deepCopy();
                    System.out.println("longer run: " + repeatLensFound);
                    repeatLensFound = 0;
                }
                repeatLensFound++;
                //longestRuns.add(currentRun.deepCopy());
            }

            //exit up to the other runs, this one's done!
            return false;
        }

        //Look at the edges in this vertex.
        for (int i = 0; i <= MAX_EDGE; i++) {
            if (hasEdge(startVertex, i)) {
                toggleEdgePair(startVertex, i);
                currentRun.addDomino(new Domino(startVertex, i));
                if (traverse(i))
                    return true;
                currentRun.popEnd();
                toggleEdgePair(startVertex, i);
            }
        }

        return false;
    }

    public DominoRun getLongestPath() {
        if (!pathsAreCurrent)
            recalculatePaths();

        return longest;
    }

    public DominoRun getMostPointPath() {
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
            throw new AssertionError("Out of bounds! Bad domino! Max edge = "
                                        + MAX_EDGE + ". Found edges = " + (v1) + " " + (v2));
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
            if (v1 != v2) {
                graph[v1].toggleEdge(v2);
                graph[v2].toggleEdge(v1);
            }
            else {
                graph[v1].toggleEdge(v2);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new AssertionError("Out of bounds! Bad domino!");
        }
    }

    private boolean hasEdge(int v1, int v2) {
        return graph[v1].hasEdge(v2) && graph[v2].hasEdge(v1);
    }

    /**
     * Deletes an edge pair from this graph.
     * @param v1 Vertex 1
     * @param v2 Vertex 2
     */
    private void removeEdgePair(int v1, int v2) {
        try {
            if (graph[v1].hasEdge(v2)) {
                if (v1 != v2) {
                    graph[v1].toggleEdge(v2);
                    graph[v2].toggleEdge(v1);
                }
                else {
                    graph[v1].toggleEdge(v2);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new AssertionError("Out of bounds! Bad domino!");
        }
    }

    /**
     * Dequeues the longest front, has to re-calculate for the most point path.
     * @return The previous front of the longest list.
     */
    public Domino dequeueLongest() {
        Domino retVal;

        retVal = getLongestPath().popFront();
        target = retVal.getVal2();
        removeEdgePair(retVal.getVal1(), retVal.getVal2());
        recalculatePaths();

        return retVal;
    }

    /**
     * Dequeues the most point front, has to re-calculate for the longest path.
     * @return The previous front of the most point path.
     */
    public Domino dequeueMostPoints() {
        Domino retVal;

        retVal = getMostPointPath().popFront();
        target = retVal.getVal2();
        removeEdgePair(retVal.getVal1(), retVal.getVal2());
        recalculatePaths();

        return retVal;
    }
}
