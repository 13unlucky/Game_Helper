package dominosource;

/**
 * Created by Jacob on 2/11/2015.
 * A domino: defined as value 1, value 2, and sum of values.
 * TODO add more functionality.
 */
public class Domino {
    private final int val1;
    private final int val2;
    private final int sum;

    /**
     * Constructor. Creates the Domino.
     * @param value1 Pair value 1.
     * @param value2 Pair value 2.
     */
    Domino(int value1, int value2) {
        val1 = value1;
        val2 = value2;
        sum = getVal1() + getVal2();
    }

    public int getVal1() {
        return val1;
    }

    public int getVal2() {
        return val2;
    }

    public int getSum() {
        return sum;
    }
}
