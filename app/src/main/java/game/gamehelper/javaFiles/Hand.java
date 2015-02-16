package game.gamehelper.javaFiles;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import game.gamehelper.MainWindow;
import game.gamehelper.javaFiles.Domino;

/**
 * Created by Jacob on 2/11/2015.
 * A hand of Dominoes.
 * TODO add remove/add domino functionality.
 */
public class Hand {
    private ArrayList<Domino> handAll;
    private List<Domino> currentHand;

    private int sumCurrentHand = 0;
    private int totalPointsHand = 0;
    private int largestDouble = -1;
    private int totalDominos;
    private static int LARGEST_DOUBLE;

    //Initializes the hand
    public Hand(int[][] tileList, Context context) {
        handAll = new ArrayList<Domino>();
        totalDominos = MainWindow.totalTiles;

        //create list of tiles
        for(int[] i : tileList){
            if( totalDominos-- <= 0 )
                break;

            handAll.add(new Domino(i[0], i[1],context));
            totalPointsHand += i[0] + i[1];

            //find largest double
            if(i[0] == i[1] && i[0] > largestDouble)
                largestDouble = i[0];
        }

        totalDominos = MainWindow.totalTiles;

        //currentHand = new ArrayList<Domino>();
        //sumCurrentHand = 0;

        LARGEST_DOUBLE = largestDouble;
    }

    //Adds a domino to the hand, increases point value.
    public void addDomino(Domino d) {
        //if (d.getVal1() > LARGEST_DOUBLE || d.getVal2() > LARGEST_DOUBLE)
        //    throw new AssertionError("Attempted to add double greater than stored maximum double.");

        handAll.add(d);
        //currentHand.add(d);
        //sumCurrentHand = getSumCurrentHand() + d.getSum();
        totalPointsHand = getTotalPointsHand() + d.getSum();
    }

    //Removes a domino to hand if it exists.
    public void removeDomino(Domino d) {
        for(Domino a : handAll){
            if(a.compareTo(d)) {
                handAll.remove(a);
                break;
            }
        }
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

}
