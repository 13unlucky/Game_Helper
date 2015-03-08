package game.gamehelper.javaFiles;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;

import game.gamehelper.MainWindow;
import game.gamehelper.javaFiles.Domino;
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

    private DominoGraph runs;

    private int totalPointsHand = 0;
    private int totalDominos;
    private final int LARGEST_DOUBLE;

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

        LARGEST_DOUBLE = largestDouble;

        runs = new DominoGraph(this, LARGEST_DOUBLE);
    }

    //NOTE: We have to have the largest double so the pathfinding calculates a legal path.
    public Hand(int largestDouble){
        handAll = new ArrayList<Domino>();
        currentHand = new LinkedList<Domino>();
        totalDominos = 0;
        LARGEST_DOUBLE = largestDouble;

        runs = new DominoGraph(this, LARGEST_DOUBLE);
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
        return LARGEST_DOUBLE;
    }

}
