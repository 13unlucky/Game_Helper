package game.gamehelper.javaFiles;

import java.util.LinkedList;

/**
 * Created by Jacob on 2/11/2015.
 * A domino run (path)
 */
public class DominoRun {
    private LinkedList<Domino> path;
    private int length;
    private int pointVal;

    DominoRun() {
        length = 0;
        pointVal = 0;
    }

    public void addDomino(Domino d) {
        path.addLast(d);
        length++;
        pointVal += d.getSum();
    }

    public Domino popEnd() {
        length--;
        pointVal -= path.getLast().getSum();
        return path.removeLast();
    }

    public Domino popFront() {
        length--;
        pointVal -= path.getFirst().getSum();
        return path.removeFirst();
    }

    /**
     * compares two domino chains by length.
     * @param other The other domino chain to compare to.
     * @return True, if this one is longer than the other.
     */
    public boolean isLonger(DominoRun other) {
        return (getLength() > other.getLength());
    }

    /**
     * compares two domino chains by point value.
     * @param other The other domino chain to compare to.
     * @return True, if this one is worth more points than the other.
     */
    public boolean hasMorePoints(DominoRun other) {
        return (getPointVal() > other.getPointVal());
    }

    /**
     * compares two domino chains by point value and length.
     * @param other The other domino chain to compare to.
     * @return True, if this one has more points and is longer than the other one.
     */
    public boolean isBetterThan(DominoRun other) {
        return (isLonger(other) && hasMorePoints(other));
    }

    public int getLength() {
        return length;
    }

    public int numMoves() {
        return getLength();
    }

    public int getPointVal() {
        return pointVal;
    }
}
