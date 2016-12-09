package edu.illinois.Matrix;

import edu.illinois.Utils;

import java.util.ArrayList;
import java.util.Random;

import static java.util.stream.IntStream.range;

public class MotifMatrix extends WeightMatrix {

    private int motifLength;
    private double informationContentPerColumn;

    /**
     * Creates a countMatrix that may be sampled from where 
     * columns have informationContentPerColumn information content
     * and there are motifLength columns
     * @param informationContentPerColumn, information content per column
     * @param motifLength, number of columns
     */
    public MotifMatrix(double informationContentPerColumn, int motifLength) {
        this.informationContentPerColumn = informationContentPerColumn;
        this.motifLength = motifLength;
        initMatrix(motifLength, 4);
        initMotifMatrix();
    }

    /**
     * Initializes the countMatrix then randomly
     * chooses the probabilities based off of the
     * information content per column
     */
    public void initMotifMatrix() {
        initCountMatrix();
        final Random r = new Random();
        range(0,motifLength).parallel().forEach(i -> stochasticGradientDescent(r, i));
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
     * @param idx, row index of motif to perform work on
     */
    private void stochasticGradientDescent(Random r, int idx) {
        int prevStep = countSum / 4 / 1000;
        int step = prevStep > 0 ? prevStep : 5;
        int[] row = countMatrix[idx];
        int incIdx = r.nextInt(4);
        double ic = Utils.calcInformationContent(countSum, row);
        assert(ic == 0);

        int decIdx = incIdx;
        while(step > 0) {
            while (ic < informationContentPerColumn) {
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
            if(ic > informationContentPerColumn) {
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
     * @param targetIdx, target index that may not be decremented
     * @param step, amount of decrement
     * @return index to decrement
     */
    private int pickDecrementIndex(int[] row, int targetIdx, int step) {
        ArrayList<Integer> acceptableIndices = new ArrayList<>();
        for(int i = 0; i < row.length; i++) {
            if(targetIdx == i)
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
        countMatrix = new int[motifLength][4];
        int initCount = countSum / 4;
        range(0,motifLength).parallel().forEach(i ->
                range(0, 4).forEach(j -> countMatrix[i][j] = initCount));
    }

    /**
     * Use the weights to randomly select a base ml times to form a sampled motif
     * @return motif, String
     */
    public String sample(Random r) {
        StringBuilder stringBuilder = new StringBuilder();
        range(0,motifLength).forEach(i -> {
            int randomWeight = r.nextInt(countSum);
            int j = -1, k = 0;
            do {
                k += countMatrix[i][++j];
            } while(k < randomWeight);
            stringBuilder.append(Utils.ACGT[j]);
        });
        return stringBuilder.toString();
    }
}
