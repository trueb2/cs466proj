package edu.illinois;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class WeightMatrix {
    public static final int[] ACGT = {0, 1 , 2, 3};
    int[][] countMatrix;
    double[][] logProbMatrix;
    int countSum = 1000000;

    public WeightMatrix(double icpc, int ml) {
        initCountMatrix(ml);
        IntStream.range(0,ml).parallel().forEach(i -> {
            stochasticGradientDescent(icpc, i);
        });
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
     * @param icpc
     * @param idx
     */
    public void stochasticGradientDescent(double icpc, int idx) {
        int prevStep = countSum / 4 / 1000;
        int step = prevStep > 0 ? prevStep : 5;
        Random r = new Random();
        int[] row = countMatrix[idx];
        int incIdx = r.nextInt(4);
        double ic = calcInformationContent(row);
        assert(ic == 0);

        int decIdx = incIdx;
        while(step > 0) {
            while (ic < icpc) {
                decIdx = pickDecrementIndex(row, incIdx, step);
                if (decIdx == -1) {
                    prevStep = step;
                    step = prevStep / 2;
                } else {
                    row[incIdx] += step;
                    row[decIdx] -= step;
                    ic = calcInformationContent(row);
                }
            }
            prevStep = step;
            step = prevStep / 2;
            row[incIdx] -= prevStep;
            row[decIdx] += prevStep;
            ic = calcInformationContent(row);
        }
        countMatrix[idx] = row;
    }

    private int pickDecrementIndex(int[] row, int ti, int step) {
        ArrayList<Integer> acceptableIndices = new ArrayList<>();
        for(int i = 0; i < row.length; i++) {
            if(ti == i)
                continue;
            if(row[i] - step > 0)
                acceptableIndices.add(i);
        }

        if(acceptableIndices.size() == 0)
            return -1;
        else
            return acceptableIndices.get(new Random().nextInt(acceptableIndices.size()));
    }

    private double calcInformationContent(int[] counts) {
        double sum = (double) countSum;
        double log_2 = Math.log(2);
        DoubleStream probabilities = Arrays.stream(counts).mapToDouble(i -> ((double)i)/sum);
        return probabilities.map(p -> p * (Math.log(p/.25)/log_2)).sum();
    }


    public void initCountMatrix(int ml) {
        countMatrix = new int[ml][4];
        int initCount = countSum / 4;
        IntStream.range(0,ml).parallel().forEach(i -> {
            IntStream.range(0,4).forEach(j -> {
                countMatrix[i][j] = initCount;
            });
        });
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int[] b : countMatrix)
            stringBuilder.append(String.format("%d %d %d %d [%f]\n", b[0], b[1], b[2], b[3], calcInformationContent(b)));
        return stringBuilder.toString();
    }
}
