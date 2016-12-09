package edu.illinois.Matrix;

import edu.illinois.Utils;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.*;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class WeightMatrix {
    int ml;
    int[][] countMatrix;
    int countSum = 1000000;

    public WeightMatrix() {}


    /**
     * Use the weights to randomly select a base ml times to form a sampled motif
     * @return motif, String
     */
    public String sample(Random r) {
        StringBuilder stringBuilder = new StringBuilder();
        range(0,ml).forEach(i -> {
            int randomWeight = r.nextInt(countSum);
            int j = -1, k = 0;
            do {
                k += countMatrix[i][++j];
            } while(k < randomWeight);
            stringBuilder.append(Utils.ACGT[j]);
        });
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return Arrays.asList(countMatrix)
                .stream()
                .map(b -> String.format("%d %d %d %d", b[0], b[1], b[2], b[3]))
                .collect(Collectors.joining("\n"));
    }
}
