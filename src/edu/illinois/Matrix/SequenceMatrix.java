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
        rowSum = sequenceCount;
        this.sequenceLength = sequences.get(0).length();
        initMatrix(sequenceLength);
        initSequenceMatrix();
    }

    /**
     * Counts the occurrences of each base along each position of each sequence
     */
    private void initSequenceMatrix() {
        IntStream.range(0,sequenceCount).forEach(i -> {
            String sequence = sequences.get(i);
            IntStream.range(0,sequenceLength).forEach(j -> {
                int b = Utils.indexOfBase(sequence.charAt(j));
                countsMatrix[j][b]++;
            });
        });
    }

    /**
     * Returns the probability of seeing the base in the index
     * @param index, index of base
     * @param base, base in the index
     */
    public double probability(int index, int base) {
        return ((double) countsMatrix[index][base]) / ((double) rowSum);
    }
}
