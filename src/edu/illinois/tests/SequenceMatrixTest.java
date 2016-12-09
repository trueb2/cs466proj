package edu.illinois.tests;

import edu.illinois.Matrix.SequenceMatrix;
import edu.illinois.Utils;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class SequenceMatrixTest {

    @Test
    public void sequenceMatrixTest1() {
        Random r = new Random(2);
        List<String> randomSequences = IntStream.range(0,5).mapToObj(i -> Utils.randomBases(10, r)).collect(Collectors.toList());
        SequenceMatrix sm = new SequenceMatrix(randomSequences);
        assertEquals("1 1 2 1\n" +
                "2 2 1 0\n" +
                "1 0 1 3\n" +
                "1 0 3 1\n" +
                "2 1 1 1\n" +
                "1 2 0 2\n" +
                "2 1 1 1\n" +
                "2 1 1 1\n" +
                "1 1 0 3\n" +
                "1 0 2 2", sm.toString());
    }
}
