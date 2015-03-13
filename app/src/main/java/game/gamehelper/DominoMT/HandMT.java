package game.gamehelper.DominoMT;

import java.util.ArrayList;
import java.util.Stack;

import game.gamehelper.Hand;
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

public class HandMT implements Hand{
    private ArrayList<Domino> dominoHandHistory;
    private ArrayList<Domino> currentHand;

    private RunController runs;

    private int totalPointsHand = 0;
    private int totalDominos;
    private final int MAXIMUM_DOUBLE;
    private int trainHead;
    private Stack<Domino> playHistory = new Stack<>();
    private Stack<Integer> trainHeadHistory = new Stack<>();
    private Stack<Integer> positionPlayedHistory = new Stack<>();


    //Initializes the hand
    //Requires maximum double possible.
    //NOTE: We have to have the largest double so the pathfinding calculates a legal path.
    public HandMT(int[][] tileList, int totalTiles, int largestDouble) {
        dominoHandHistory = new ArrayList<Domino>();
        currentHand = new ArrayList<Domino>();
        totalDominos = totalTiles;

        //create list of tiles
        for (int[] i : tileList) {
            if (totalDominos-- <= 0)
                break;

            dominoHandHistory.add(new Domino(i[0], i[1]));
            currentHand.add(new Domino(i[0], i[1]));
            totalPointsHand += i[0] + i[1];
        }

        totalDominos = totalTiles;

        MAXIMUM_DOUBLE = largestDouble;
        trainHead = MAXIMUM_DOUBLE;

        runs = new RunController(this, MAXIMUM_DOUBLE);
    }

    //NOTE: We have to have the largest double so the pathfinding calculates a legal path.
    public HandMT(int largestDouble) {
        dominoHandHistory = new ArrayList<Domino>();
        currentHand = new ArrayList<Domino>();
        totalDominos = 0;
        MAXIMUM_DOUBLE = largestDouble;
        trainHead = MAXIMUM_DOUBLE;

        runs = new RunController(this, MAXIMUM_DOUBLE);
    }

    //Adds a domino to the hand, but only if it doesn't exist
    public void addDomino(Domino d) {
        for (Domino a : currentHand){
            if (a.compareTo(d)) {
                return;
            }
        }
        dominoHandHistory.add(d);
        currentHand.add(d);
        totalPointsHand = getTotalPointsHand() + d.getSum();
        runs.addDomino(d);
    }

    //Removes a domino to hand if it exists.
    public void removeDomino(Domino d) {
        for (Domino a : currentHand) {
            if (a.compareTo(d)) {
                currentHand.remove(a);
                totalPointsHand = getTotalPointsHand() - d.getSum();
                runs.removeDomino(a);
                break;
            }
        }
    }

    /**
     * Plays a domino based on its position in the arraylist.
     * @param position The play position; what order we made the play in.
     * @param playContext The context in which the play was made.
     */
    public void dominoPlayed(int position, GameWindowMT.WindowContext playContext) {
        //We find the "real" position, and change position to match it so we can delete it below.

        //find the longest path position.
        if (playContext == GameWindowMT.WindowContext.SHOWING_LONGEST) {
            // Converts the longest run to an array and indexes the position.
            Domino d = getLongestRun().toArray()[position];
            //finds the domino in the current hand, and sets the correct position.
            position = currentHand.indexOf(d);
        }
        //find the most points path position.
        else if (playContext == GameWindowMT.WindowContext.SHOWING_MOST_POINTS) {
            // Converts the most points run to an array and indexes the position.
            Domino d = getMostPointRun().toArray()[position];
            //finds the domino in the current hand, and sets the correct position.
            position = currentHand.indexOf(d);
        }
        //the position given is actually the correct position.
        else {
            //position = position;
        }

        //find the domino to remove, and get it from the current hand.
        Domino toRemove = currentHand.get(position);

        //add to our undo stacks.
        playHistory.push(toRemove);
        positionPlayedHistory.push(position);
        trainHeadHistory.push(trainHead);

        //we removed the train head, adjust the train head accordingly.
        if (toRemove.getVal1() == trainHead) {
            trainHead = toRemove.getVal2();
        }
        else if (toRemove.getVal2() == trainHead) {
            trainHead = toRemove.getVal1();
        }

        //removes the domino & its information from the hand.
        runs.removeDomino(toRemove);
        currentHand.remove(toRemove);
        totalPointsHand = getTotalPointsHand() - toRemove.getSum();
    }

    /**
     * Undoes a previous play. Re-sets the train head to the old one, and
     * adds the domino back in its old play position.
     */
    public void undo() {
        if (playHistory.size() == 0)
            return;

        Domino lastDomino;
        int position;

        //retrieve last move
        position = positionPlayedHistory.pop();
        trainHead = trainHeadHistory.pop();
        lastDomino = playHistory.pop();

        //add information back to hand
        currentHand.add(position, lastDomino);
        runs.reAddDomino(lastDomino, trainHead);
        totalPointsHand += lastDomino.getSum();
    }

    /**
     * Gets the longest run from our RunController.
     * @return Returns the longest run.
     */
    public DominoRun getLongestRun() {
        return runs.getLongestPath();
    }

    /**
     * Gets the most points run from our RunController.
     * @return Returns the most points run.
     */
    public DominoRun getMostPointRun() {
        return runs.getMostPointPath();
    }

    /**
     * Gets the total points of the current hand.
     * @return Returns the total points of the current hand.
     */
    @Override
    public int getTotalPointsHand() {
        return totalPointsHand;
    }

    /**
     * Converts the current hand to a domino array.
     * @return Returns the converted current hand to a domino array.
     */
    public Domino[] toArray() {
        return currentHand.toArray(new Domino[currentHand.size()]);
    }

    /**
     * Gets the maximum double value, which all dominoes with that value must be played on.
     * @return Returns maximum double (the start domino)
     */
    public int getMaxDouble(){
        return MAXIMUM_DOUBLE;
    }

    /**
     * Gets the current train head.
     * @return Returns the current train head.
     */
    public int getTrainHead() {
        return trainHead;
    }

    /*
     * Changes current domino head based on manual input in GameWindow
     */
    public void setTrainHead(int head){
        trainHead = head;
        runs.setTrainHead(head);
    }



}
