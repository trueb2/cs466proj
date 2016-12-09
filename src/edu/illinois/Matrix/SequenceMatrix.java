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
        this.sequenceLength = sequences.get(0).length();
        initMatrix();
        rows = sequenceCount;
        cols = 4;
    }

    /**
     * Counts the occurrences of each base along each position of each sequence
     */
    public void initMatrix() {
        IntStream.range(0,sequenceCount).parallel().forEach(i -> {
            String sequence = sequences.get(i);
            IntStream.range(0,sequenceLength).forEach(j -> {
                int b = Utils.indexOfBase(sequence.charAt(j));
                countMatrix[i][b]++;
            });
        });
    }
}
