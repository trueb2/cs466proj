package edu.illinois.tests;

import edu.illinois.finders.GibbsSampler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.illinois.SequenceGenerator.generateRandomSequences;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class GibbsSamplerTest {

    @Test
    public void readFastaFileTest1() {
        GibbsSampler gs = new GibbsSampler("test_sequences.fa","test_motiflength.txt", "out/test/data");
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
        GibbsSampler gs = new GibbsSampler("test_sequences.fa","test_motiflength.txt", "out/test/data");
        gs.find(100, new Random(1));
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