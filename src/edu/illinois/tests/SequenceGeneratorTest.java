package edu.illinois.tests;

import org.junit.Test;

import java.util.ArrayList;

import static edu.illinois.SequenceGenerator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class SequenceGeneratorTest {

    @Test
    public void generateRandomSequences1() {
        ArrayList<String> sequences = generateRandomSequences(2,10);
        assertEquals(2,sequences.size());
        assertEquals(10,sequences.get(0).length());
        assertEquals(10,sequences.get(1).length());
    }

    @Test
    public void generateRandomMotif1() {
        String motif = generateMotif(1, 10);
        assertEquals(10, motif.length());
    }

    @Test
    public void generateBindingSites1() {
        ArrayList<String> sequences = generateRandomSequences(10,20);
        String motif = generateMotif(1,4);
        ArrayList<Integer> bindingSites = generateBindingSites(sequences,motif);
        assertTrue(bindingSites.stream().max(Integer::max).get() < 16);
        assertTrue(bindingSites.stream().min(Integer::min).get() >= 0);
    }
}