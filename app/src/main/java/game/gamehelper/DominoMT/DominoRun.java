package game.gamehelper.DominoMT;

import java.util.LinkedList;

/**
 * Created by Jacob on 2/11/2015.
 * A domino run (path)
 */
public class DominoRun {
    private LinkedList<Domino> path;
    private int pointVal;

    DominoRun() {
        pointVal = 0;
        path = new LinkedList<Domino>();
    }

    public void addDomino(Domino d) {
        path.addLast(d);
        pointVal += d.getSum();
    }

    public void addDominoFront(Domino d) {
        path.addFirst(d);
        pointVal += d.getSum();
    }

    public Domino peekFront() {
        return path.peek();
    }

    public Domino popEnd() {
        pointVal -= path.getLast().getSum();
        return path.removeLast();
    }

    public Domino popFront() {
        pointVal -= path.getFirst().getSum();
        return path.removeFirst();
    }

    public void clear() {
        path.clear();
    }

    /**
     * compares two domino chains by length.
     * @param other The other domino chain to compare to.
     * @return True, if this one is longer than the other.
     */
    public boolean isLongerThan(DominoRun other) {
        return (this.getLength() > other.getLength());
    }

    /**
     * compares two domino chains by length.
     * @param other The other domino chain to compare to.
     * @return True, if this one is shorter than the other.
     */
    public boolean isShorterThan(DominoRun other) {
        return (this.getLength() > other.getLength());
    }

    /**
     * compares two domino chains by point value.
     * @param other The other domino chain to compare to.
     * @return True, if this one is worth more points than the other.
     */
    public boolean hasMorePointsThan(DominoRun other) {
        return (this.getPointVal() > other.getPointVal());
    }

    /**
     * compares two domino chains by point value and length.
     * @param other The other domino chain to compare to.
     * @return True, if this one has more points and is longer than the other one.
     */
    public boolean isBetterThan(DominoRun other) {
        return (this.isLongerThan(other) && this.hasMorePointsThan(other));
    }

    public int getLength() {
        return path.size();
    }

    public int numMoves() {
        return this.getLength();
    }

    public int getPointVal() {
        return pointVal;
    }

    //Deep copy of this run.
    public DominoRun deepCopy() {
        DominoRun copy = new DominoRun();

        for (Domino d : path) {
            copy.addDomino(d);
        }
        return copy;
    }

    //returns these dominoes as an array.
    public Domino[] toArray() {
        return path.toArray(new Domino[path.size()]);
    }
}
