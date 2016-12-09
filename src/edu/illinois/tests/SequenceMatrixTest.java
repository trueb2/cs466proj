package edu.illinois.tests;

import edu.illinois.Matrix.SequenceMatrix;
import edu.illinois.Utils;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SequenceMatrixTest {

    @Test
    public void sequenceMatrixTest1() {
        Random r = new Random();
        List<String> randomSequences = IntStream.range(0,5).mapToObj(i -> Utils.randomBases(10, r)).collect(Collectors.toList());
        SequenceMatrix sm = new SequenceMatrix(randomSequences);
    }
}
