package edu.illinois.tests;

import java.util.ArrayList;

import static edu.illinois.SequenceGenerator.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class SequenceGeneratorTest {

    @org.junit.Test
    public void generateRandomSequences1() throws Exception {
        ArrayList<String> sequences = generateRandomSequences(2,10);
        assertEquals(2,sequences.size());
        assertEquals(10,sequences.get(0).length());
        assertEquals(10,sequences.get(1).length());
    }

    @org.junit.Test
    public void generateRandomMotif1() throws Exception {
        String motif = generateRandomMotif(1, 10);
        assertEquals(10, motif.length());
    }
}