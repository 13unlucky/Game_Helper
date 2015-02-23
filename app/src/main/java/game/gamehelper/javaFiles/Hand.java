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

    private int sumCurrentHand = 0;
    private int totalPointsHand = 0;
    private int totalDominos;
    private final int LARGEST_DOUBLE;

    //Initializes the hand
    //Requires maximum double possible.
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

    public Hand(){
        handAll = new ArrayList<Domino>();
        currentHand = new LinkedList<Domino>();
        totalDominos = 0;
        LARGEST_DOUBLE = -1;

    }

    //Adds a domino to the hand, increases point value.
    public void addDomino(Domino d) {
        //if (d.getVal1() > LARGEST_DOUBLE || d.getVal2() > LARGEST_DOUBLE)
        //    throw new AssertionError("Attempted to add double greater than stored maximum double.");

        handAll.add(d);
        totalPointsHand = getTotalPointsHand() + d.getSum();
    }

    //Removes a domino to hand if it exists.
    public void removeDomino(Domino d) {
        for (Domino a : handAll){
            if (a.compareTo(d)) {
                handAll.remove(a);
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

    public int getSumCurrentHand() {
        return sumCurrentHand;
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
