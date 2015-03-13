package game.gamehelper.javaFiles;

import java.util.LinkedList;

/**
 * Created by Jacob on 3/6/2015.
 * Contains run-calculating related algorithms.
 */
public class RunController {
    private final int MAX_EDGE;
    private DominoGraph graph;
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
     * Generates a RunController from a domino edge array.
     * @param h The hand to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    RunController(Hand h, int startDouble) {
        this (h.getMaxDouble(), h.toArray(), h.getMaxDouble());
    }

    /**
     * Generates a RunController from a domino edge array.
     * @param maximumDouble The biggest double possible (if double 8) -> 8.
     * @param edges The array of edges to use to initialize the graph.
     * @param startDouble The starting double to target.
     */
    RunController(int maximumDouble, Domino edges[], int startDouble) {
        MAX_EDGE = maximumDouble;

        graph = new DominoGraph(maximumDouble, edges);
        totalEdgeNum = edges.length;

        //debug variables
        repeatLensFound = 0;
        repeatPointsFound = 0;
        numVisited = 0;
        pathsFound = 0;

        pathsAreCurrent = false;
        longest = new DominoRun();
        mostPoints = new DominoRun();
        target = startDouble;
        mostPointRuns = new LinkedList<DominoRun>();
        longestRuns = new LinkedList<DominoRun>();
        currentRun = new DominoRun();
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
        if (graph.hasEdge(MAX_EDGE, MAX_EDGE)) {
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
        boolean startEdges[] = graph.getMaxEdges();

        //We blank out the start edges.
        for (int i = MAX_EDGE; i >= 0; i--) {
            graph.removeEdgePair(MAX_EDGE, i);
        }

        //We need to start on the main domino, so we go through each possible lead-off individually.
        //Remember, edges that map to the main domino must be played on the main domino.
        if (target == MAX_EDGE) {
            for (int i = 0; i <= MAX_EDGE; i++) {
                if (startEdges[i]) {
                    currentRun.clear();
                    graph.addEdgePair(i, MAX_EDGE);
                    traverse(MAX_EDGE);
                    graph.removeEdgePair(i, MAX_EDGE);
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
                graph.addEdgePair(MAX_EDGE, i);
        }

        //simple heuristic to remove copy runs. Still will need more processing.
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
        if (graph.getEdgeNum(startVertex) == 0) {
            pathsFound++;
            //this run isn't useful in terms of points, skip it.
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

            //this run isn't useful in terms of length, skip it.
            if (longest.isLongerThan(currentRun)) {
                ;
            }
            //this run is so useful in terms of length, we have to get rid of the other runs.
            else if (currentRun.isLongerThan(longest)) {
                longestRuns.clear();
                longestRuns.add(currentRun.deepCopy());
                longest = currentRun.deepCopy();

                //early exit if we touch everything.
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
            }

            //exit up to the other runs, this one's done!
            return false;
        }

        //Look at the edges in this vertex.
        for (int i = 0; i <= MAX_EDGE; i++) {
            if (graph.hasEdge(startVertex, i)) {
                graph.toggleEdgePair(startVertex, i);
                currentRun.addDomino(new Domino(startVertex, i));
                //we've finished! We've visited every vertex in the graph, we can leave early.
                if (traverse(i)) {
                    graph.toggleEdgePair(startVertex, i);
                    return true;
                }
                currentRun.popEnd();
                graph.toggleEdgePair(startVertex, i);
            }
        }

        return false;
    }

    //returns the longest path, does pathfinding if necessary.
    public DominoRun getLongestPath() {
        if (!pathsAreCurrent)
            recalculatePaths();

        return longest;
    }

    //Returns the most-points path, does pathfinding if necessary.
    public DominoRun getMostPointPath() {
        if (!pathsAreCurrent)
            recalculatePaths();

        return mostPoints;
    }

    /**
     * Adds another domino to this graph.
     * @param d The domino to add.
     */
    public void addDomino(Domino d) {
        if (d.getVal1() > MAX_EDGE || d.getVal2() > MAX_EDGE)
            throw new AssertionError("New domino is too large.");

        //re-set paths if the domino isn't already in the graph.
        //TODO: Add BFS to determine if we need to re-calculate or not
        if (!graph.hasEdge(d.getVal1(), d.getVal2())) {
            pathsAreCurrent = false;
            totalEdgeNum++;
        }

        graph.addEdgePair(d.getVal1(), d.getVal2());
    }

    /**
     * Re-adds a domino to this graph.
     * @param d The domino to re-add.
     * @param targetVal The new target value.
     */
    public void reAddDomino(Domino d, int targetVal) {
        if (d.getVal1() > MAX_EDGE || d.getVal2() > MAX_EDGE)
            throw new AssertionError("New domino is too large.");

        pathsAreCurrent = false;
        totalEdgeNum++;
        target = targetVal;

        graph.addEdgePair(d.getVal1(), d.getVal2());
    }

    /**
     * Removes a domino in this graph.
     * Will only throw exceptions when the domino is larger than the maximum domino.
     * @param d The domino to remove.
     */
    public void removeDomino(Domino d) {
        if (d.getVal1() > MAX_EDGE || d.getVal2() > MAX_EDGE)
            throw new AssertionError("Tried to delete domino larger than max domino.");

        //re-set paths if we're deleting something in the graph
        if (graph.hasEdge(d.getVal1(), d.getVal2())) {
            //check to see if we can just de-queue the front of the pre-calculated runs.
            if (pathsAreCurrent && d.compareTo(getMostPointPath().peekFront())) {
                dequeueMostPoints();
            }
            else if (pathsAreCurrent && d.compareTo(getLongestPath().peekFront())) {
                dequeueLongest();
            }
            //since we can't just de-queue the front, we have to delete the value and re-calculate.
            else {
                pathsAreCurrent = false;
                totalEdgeNum--;

                //check to make sure we didn't delete the target, and if so, re-adjust target.
                if (d.getVal1() == target) {
                    target = d.getVal2();
                }
                else if (d.getVal2() == target) {
                    target = d.getVal1();
                }

                graph.removeEdgePair(d.getVal1(), d.getVal2());
            }
        }
    }

    /**
     * Dequeues the longest front, has to re-calculate for the most point path if it wasn't the other path's front.
     * @return The previous front of the longest list.
     */
    public Domino dequeueLongest() {
        Domino retVal;

        //get the front domino
        retVal = getLongestPath().popFront();

        //remove the old domino, set the new target.
        graph.removeEdgePair(retVal.getVal1(), retVal.getVal2());
        target = retVal.getOtherVal(target);
        totalEdgeNum--;

        //if it's not the same as the most point path's front, re-calculate runs.
        if (!retVal.compareTo(getMostPointPath().popFront()))
            recalculatePaths();

        return retVal;
    }

    /**
     * Dequeues the most point front, has to re-calculate for the longest path if it wasn't the other path's front.
     * @return The previous front of the most point path.
     */
    public Domino dequeueMostPoints() {
        Domino retVal;

        //get the front domino
        retVal = getMostPointPath().popFront();

        //remove the old domino, set the new target.
        graph.removeEdgePair(retVal.getVal1(), retVal.getVal2());
        target = retVal.getOtherVal(target);
        totalEdgeNum--;

        //If it's not the same as the longest path's front, re-calculate runs.
        if (!retVal.compareTo(getLongestPath().popFront()))
            recalculatePaths();

        return retVal;
    }

    public void setTrainHead(int head){
        target = head;
        recalculatePaths();
    }
}
