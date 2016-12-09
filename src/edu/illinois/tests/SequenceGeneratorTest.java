package edu.illinois.tests;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import static edu.illinois.SequenceGenerator.*;
import static org.junit.Assert.assertEquals;

public class SequenceGeneratorTest {

    @Test
    public void generateRandomSequences1() {
        List<String> sequences = generateRandomSequences(2, 10, new Random());
        assertEquals(2, sequences.size());
        assertEquals(10, sequences.get(0).length());
        assertEquals(10, sequences.get(1).length());
    }
}