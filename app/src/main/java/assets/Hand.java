package assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 2/11/2015.
 * A hand of Dominoes.
 * TODO add remove/add domino functionality.
 */
public class Hand {
    private ArrayList<Domino> handAll;
    private List<Domino> currentHand;

    private int sumCurrentHand;
    private int totalPointsHand;
    private final int LARGEST_DOUBLE;

    //Initializes the hand
    Hand(int largestDouble) {
        handAll = new ArrayList<Domino>();
        currentHand = new ArrayList<Domino>();

        sumCurrentHand = 0;
        totalPointsHand = 0;
        LARGEST_DOUBLE = largestDouble;
    }

    //Adds a domino to the hand, increases point value.
    public void addDomino(Domino d) {
        if (d.getVal1() > LARGEST_DOUBLE || d.getVal2() > LARGEST_DOUBLE)
            throw new AssertionError("Attempted to add double greater than stored maximum double.");

        handAll.add(d);
        currentHand.add(d);
        sumCurrentHand = getSumCurrentHand() + d.getSum();
        totalPointsHand = getTotalPointsHand() + d.getSum();
    }

    //Removes a domino to hand if it exists.
    public void removeDomino(Domino d) {

    }

    public int getSumCurrentHand() {
        return sumCurrentHand;
    }

    public int getTotalPointsHand() {
        return totalPointsHand;
    }

    public Domino[] toArray() {
        return currentHand.toArray(new Domino[currentHand.size()]);
    }
}
