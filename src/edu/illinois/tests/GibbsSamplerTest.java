package edu.illinois.tests;

import edu.illinois.benchmarks.OverlapBenchmark;
import edu.illinois.benchmarks.PrintSitesBenchmark;
import edu.illinois.benchmarks.RelativeEntropyBenchmark;
import edu.illinois.finders.GibbsSampler;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class GibbsSamplerTest {

    private static final String OUTPUT_DIRECTORY = "out/data/test/";

    @Test
    public void readFastaFileTest1() {
        GibbsSampler gs = new GibbsSampler("sequences.fa", "motiflength.txt", OUTPUT_DIRECTORY);
        assertEquals(10, gs.getSequenceCount());
        List<String> sequences = gs.getSequences();
        assertEquals("TTTAGGCCTATATTCCTCTTAGTTCGGTATCTGGCGCCGGCACACGTCCCAGAATAATCACCCAAGGG" +
                "CGAGCCCGTTAGAATAATTGTAACCGCCGCATGTCTACAGCAACATCCCACAACGATACCGCGGGAAACAACTCCGCAAACA" +
                "TCTGACCAGTATAGTATACGGGGATATGTTTCTGCATGTGCAGGGGTCACCCGGTCCCGAACGCAAATTCCCAGTCCTTATC" +
                "TCAGCGGCGGTATCTGATGGGATCACTCTGGTGGCATATGTGTTGTTTATAAGTATATCATAGTGTTTTGAAAATCAAATGC" +
                "TGGGGCGACCCGAGAGTATCGGCTTCGAGGTAATGGCTGTCGTAGAAAACGATCAAGAATACAACTTGGACTGAGCCGGGAT" +
                "CGGGTAGGCTGGGGTTACTTGGACGATCTCAGCAAAATGCACATCCGCTCAACCGTGTCACGCCTACATTATTACCGTCTAC" +
                "TCTACTTAGCGAAGGTGAGGGC", sequences.get(0));
        assertEquals("AGGGGTGCTGAAGTTATGTATAACAGTCTACATACTGACCTGGACATCGTAGGCGCTAGTGAGTCCTG" +
                "GCACCCCCTGACTTCGGTATGAGTACGGATTAAAACGGGGCACGATAACACCTCGTACGACGCAAAGCGTAGTAGTTTTACG" +
                "TAAGCGTACACTTACGAGGTCGGCTTGGCGCCAGCTGCGGACAGCGGTATATTTGTTTTATTGGGACGTTATGGCTCGTCGC" +
                "ACCGTTCATCGTACGAAACTTAAATCTCTGTCGCCCGTTCCCAGCGATATAGCGCATAGCCCGGCCCGTGTTGGGAGAAGGG" +
                "CTGGCGCAACCTCCTGTTAATTGGAGGTATATGTCAGACTTAATGCAGGATTACGAAAATCCACTTCAGACTCAGCAACAAT" +
                "GCTATTGGCCGCACTCAGCACCCCCCCGTTCGCTCATGTACACACTCCACTAATTCTTACCCTTTCTCAAGATGACCATCTG" +
                "TTGTCTCAACTCGGTGTGTCAG", sequences.get(3));
    }


    @Test
    public void findTest1() {
        GibbsSampler gs = new GibbsSampler("sequences.fa", "motiflength.txt", OUTPUT_DIRECTORY);
        gs.find(100, 500, new Random(1));

        PrintSitesBenchmark.printSitesBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);

        OverlapBenchmark.overlapBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);

        RelativeEntropyBenchmark.relativeEntropyBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);
    }
}