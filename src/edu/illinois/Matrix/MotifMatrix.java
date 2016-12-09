package edu.illinois.Matrix;

import edu.illinois.Utils;

import java.util.ArrayList;
import java.util.Random;

import static java.util.stream.IntStream.range;

/**
 * Created by Jacob on 12/8/2016.
 */
public class MotifMatrix extends WeightMatrix {

    public MotifMatrix(double icpc, int ml) {
        initMotifMatrix(icpc, ml);
    }

    private void initMotifMatrix(double icpc, int ml) {
        this.ml = ml;
        initCountMatrix();
        range(0,ml).parallel().forEach(i -> stochasticGradientDescent(icpc, i));
    }

    /**
     * Call this upon each row of the countMatrix
     * 1. Picks a random base to emphasize
     * 2. Maintains an epoch and step counter to add less as epochs go on
     * 3. Randomly picks a base other than the target base and takes step
     *      counts from it and gives it to the target base
 *     4. Calculates the Information Content after each loop
     *     Loop terminates after the Information Content has been
     *     surpassed through taking a step of size 1. If a step of size
     *     greater than 1 surpasses the threshold, then the
     *     epoch will be undone.
     * @param icpc, information content per column
     * @param idx, row index of motif to perform work on
     */
    private void stochasticGradientDescent(double icpc, int idx) {
        int prevStep = countSum / 4 / 1000;
        int step = prevStep > 0 ? prevStep : 5;
        Random r = new Random();
        int[] row = countMatrix[idx];
        int incIdx = r.nextInt(4);
        double ic = Utils.calcInformationContent(countSum, row);
        assert(ic == 0);

        int decIdx = incIdx;
        while(step > 0) {
            while (ic < icpc) {
                decIdx = pickDecrementIndex(row, incIdx, step);
                if (decIdx == -1) {
                    prevStep = step;
                    step = prevStep / 2;
                    decIdx = incIdx;
                } else {
                    row[incIdx] += step;
                    row[decIdx] -= step;
                    ic = Utils.calcInformationContent(countSum, row);
                }
            }
            prevStep = step;
            step = prevStep / 2;
            if(ic > icpc) {
                row[incIdx] -= step;
                row[decIdx] += step;
            }
            ic = Utils.calcInformationContent(countSum, row);
        }
        countMatrix[idx] = row;
    }

    /**
     * Picks the index in a row that may be decremented without going negative
     * @param row, row of counts
     * @param ti, target index that may not be decremented
     * @param step, amount of decrement
     * @return index to decrement
     */
    private int pickDecrementIndex(int[] row, int ti, int step) {
        ArrayList<Integer> acceptableIndices = new ArrayList<>();
        for(int i = 0; i < row.length; i++) {
            if(ti == i)
                continue;
            if(row[i] - step >= 0)
                acceptableIndices.add(i);
        }

        if(acceptableIndices.size() == 0)
            return -1;
        else
            return acceptableIndices.get(new Random().nextInt(acceptableIndices.size()));
    }

    /**
     * Initializes the countMatrix with the countSum/4 value
     */
    public void initCountMatrix() {
        countMatrix = new int[ml][4];
        int initCount = countSum / 4;
        range(0,ml).parallel().forEach(i ->
                range(0, 4).forEach(j -> countMatrix[i][j] = initCount));
    }
}
