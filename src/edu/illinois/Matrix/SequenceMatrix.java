package edu.illinois.Matrix;

import edu.illinois.Utils;

import java.util.List;
import java.util.stream.IntStream;

public class SequenceMatrix extends WeightMatrix {

    private final List<String> sequences;
    private final int sequenceCount;
    private final int sequenceLength;

    public SequenceMatrix(List<String> sequences) {
        this.sequences = sequences;
        this.sequenceCount = sequences.size();
        countSum = sequenceCount;
        this.sequenceLength = sequences.get(0).length();
        initMatrix(sequenceLength, 4);
        initSequenceMatrix();
    }

    public SequenceMatrix(int rows) {
        sequences = null;
        sequenceCount = 4;
        sequenceLength = rows;
        initMatrix(rows, 4);
        IntStream.range(0,rows).parallel().forEach(i -> {
            IntStream.range(0,4).forEach(j -> {
                countMatrix[i][j] = 1;
            });
        });
    }

    /**
     * Counts the occurrences of each base along each position of each sequence
     */
    public void initSequenceMatrix() {
        IntStream.range(0,sequenceCount).forEach(i -> {
            String sequence = sequences.get(i);
            IntStream.range(0,sequenceLength).forEach(j -> {
                int b = Utils.indexOfBase(sequence.charAt(j));
                countMatrix[j][b]++;
            });
        });
    }

    /**
     * Returns the probability of seeing the base in the index
     * @param index, index of base
     * @param base, base in the index
     */
    public double probability(int index, int base) {
        return ((double) countMatrix[index][base]) / ((double) countSum);
    }
}
