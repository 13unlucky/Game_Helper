package game.gamehelper.javaFiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
/**
 * Author History
 * Jacob
 * Mark
 * Jacob
 */

/**
 * Created by Jacob on 2/11/2015.
 * A hand of Dominoes.
 * TODO add remove/add domino functionality.
 */

public class Hand {
    private ArrayList<Domino> handAll;
    private LinkedList<Domino> currentHand;

    private RunController runs;

    private int totalPointsHand = 0;
    private int totalDominos;
    private int largestDouble;
    private Stack<Domino> playHistory = new Stack<>();
    private Stack<Integer> trainHeadHistory = new Stack<>();
    private Stack<Integer> positionPlayedHistory = new Stack<>();

    //Initializes the hand
    //Requires maximum double possible.
    //NOTE: We have to have the largest double so the pathfinding calculates a legal path.
    public Hand(int[][] tileList, int totalTiles, int largestDouble) {
        handAll = new ArrayList<Domino>();
        currentHand = new LinkedList<Domino>();
        totalDominos = totalTiles;

        //create list of tiles
        for (int[] i : tileList) {
            if (totalDominos-- <= 0)
                break;

            handAll.add(new Domino(i[0], i[1]));
            currentHand.add(new Domino(i[0], i[1]));
            totalPointsHand += i[0] + i[1];
        }

        totalDominos = totalTiles;

        this.largestDouble = largestDouble;

        runs = new RunController(this, this.largestDouble);
    }

    //NOTE: We have to have the largest double so the pathfinding calculates a legal path.
    public Hand(int largestDouble){
        handAll = new ArrayList<Domino>();
        currentHand = new LinkedList<Domino>();
        totalDominos = 0;
        this.largestDouble = largestDouble;

        runs = new RunController(this, this.largestDouble);
    }

    //Adds a domino to the hand, but only if it doesn't exist
    public void addDomino(Domino d) {
        for (Domino a : handAll){
            if (a.compareTo(d)) {
                return;
            }
        }
        handAll.add(d);
        totalPointsHand = getTotalPointsHand() + d.getSum();
        runs.addDomino(d);
    }

    //Removes a domino to hand if it exists.
    public void removeDomino(Domino d) {
        for (Domino a : handAll){
            if (a.compareTo(d)) {
                handAll.remove(a);
                totalPointsHand = getTotalPointsHand() - d.getSum();
                runs.removeDomino(a);
                break;
            }
        }
    }

    public void dominoPlayed(int position) {
        trainHeadHistory.push(largestDouble);
        playHistory.push(handAll.get(position));
        positionPlayedHistory.push(position);

        //replace largestDouble with the new train head and remove domino
        largestDouble = largestDouble == (handAll.get(position).getVal1()) ?
                handAll.get(position).getVal2() : handAll.get(position).getVal1();

        runs.removeDomino(handAll.get(position));
        handAll.remove(position);
        totalPointsHand = getTotalPointsHand() - playHistory.peek().getSum();
    }

    public void undo(){
        if(playHistory.size() == 0)
            return;

        Domino lastDomino;
        int position;

        //retrieve last move
        position = positionPlayedHistory.pop();
        largestDouble = trainHeadHistory.pop();
        lastDomino = playHistory.pop();

        //add information back to hand
        handAll.add(position, lastDomino);
        runs.addDomino(lastDomino);
    }

    public DominoRun getLongestRun() {
        return runs.getLongestPath();
    }

    public DominoRun getMostPointRun() {
        return runs.getMostPointPath();
    }

    public int getTotalPointsHand() {
        return totalPointsHand;
    }

    public Domino[] toArray() {
        return handAll.toArray(new Domino[handAll.size()]);
    }

    public int getLargestDouble(){
        return largestDouble;
    }

}
