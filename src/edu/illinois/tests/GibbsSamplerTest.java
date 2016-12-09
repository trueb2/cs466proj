package edu.illinois.tests;

import edu.illinois.benchmarks.Benchmark;
import edu.illinois.benchmarks.OverlapBenchmark;
import edu.illinois.benchmarks.RelativeEntropy;
import edu.illinois.finders.GibbsSampler;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static edu.illinois.SequenceGenerator.generateRandomSequences;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class GibbsSamplerTest {

    public static final String OUTPUT_DIRECTORY = "out/data/test/";

    @Test
    public void readFastaFileTest1() {
        GibbsSampler gs = new GibbsSampler("sequences.fa","motiflength.txt", OUTPUT_DIRECTORY);
        assertEquals(10,gs.getSequenceCount());
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
        GibbsSampler gs = new GibbsSampler("sequences.fa","motiflength.txt", OUTPUT_DIRECTORY);
        gs.find(10000, 500, new Random(1));
        try {
            Benchmark b = new Benchmark(OUTPUT_DIRECTORY) {
                public void benchmark() {
                    String s = getSites().stream()
                            .map(i -> i.toString())
                            .collect(Collectors.joining(" "));
                    System.out.println(s);
                }
            };
            b.benchmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            OverlapBenchmark ob = new OverlapBenchmark(OUTPUT_DIRECTORY,8);
            ob.benchmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
//
//    @Test
//    public void findTest() {
//        List<String> seq = generateRandomSequences(5,20);
//        String motif = "GGGGAGGGG";
//        Random rand = new Random(System.currentTimeMillis());
//        List<Integer> correct = new ArrayList<>();
//        for(int i = 0; i < 5; i++) {
//            int ran = rand.nextInt( 20 );
//            correct.add(ran);
//            seq.set( i, seq.get(i).substring(0,ran).concat(motif).concat( seq.get(i).substring(ran) ) );
//        }
//        System.out.println("============= Correct Answer=============");
//        System.out.println(correct);
//
//        GibbsSampler gs = new GibbsSampler();
//        gs.setSequences(seq);
//        gs.setMotifLength(motif.length());
//        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
//        assertTrue(correct.containsAll( gs.gibbsSamplingfinder(15,"./")));
//    }
}