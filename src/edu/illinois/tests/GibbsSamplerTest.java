package edu.illinois.tests;

import edu.illinois.benchmarks.OverlapBenchmark;
import edu.illinois.benchmarks.PrintSitesBenchmark;
import edu.illinois.benchmarks.RelativeEntropyBenchmark;
import edu.illinois.finders.GibbsSampler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
    public void findTest1() throws IOException {
        //Switch the output stream
        PrintStream old = System.out;
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        //Do sampling and benchmarks
        GibbsSampler gs = new GibbsSampler("sequences.fa", "motiflength.txt", OUTPUT_DIRECTORY);
        gs.find(100, 500, new Random(1));
        PrintSitesBenchmark.printSitesBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);
        OverlapBenchmark.overlapBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);
        RelativeEntropyBenchmark.relativeEntropyBenchmark(GibbsSamplerTest.OUTPUT_DIRECTORY);

        //Compare output with expected
        String expected = "======== Maximum Information Content :: 2.0 =========\n" +
                "\n" +
                "Performing :: PrintBenchmark\n" +
                "============Actual==============\n" +
                "105 488 11 386 24 198 402 438 330 270\n" +
                "============Predicted==============\n" +
                "105 488 11 386 24 198 402 438 330 270\n" +
                "\n" +
                "Performing :: OverlapBenchmark\n" +
                "Number of overlapping motifs: 10\n" +
                "Number of matched bases: 80\n" +
                "\n" +
                "Performing :: RelativeEntropyBenchmark\n" +
                "Positional relative entropy values = \n" +
                "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
                "Total relative entropy D(acc|predicted) = \n" +
                "0.0\n" +
                "\n";
        String actual = out.toString().replaceAll("\\r", "");
        actual = actual.substring(actual.length() - expected.length());
        assertEquals(actual.length(), expected.length());
        assertEquals(expected, actual);

        //Reset System.out
        System.setOut(old);
        out.close();
    }
}