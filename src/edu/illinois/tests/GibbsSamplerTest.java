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
    public void readFAFileTest1() {
        GibbsSampler mf = new GibbsSampler();
        List<String> readFastaFile = mf.readFastaFile("./test.fa");
        assertEquals(2,readFastaFile.size());
        assertEquals("MTEITAAMVKELRESTGAGMMDCKNALSETNGDFDKAVQLLREKGLGKAAKKADRLAAEGLVSVKVSDDFTIAAMRPSYLSYEDLDMTFVENEYKALVAELEKENEERRRLKDPNKPEHKIPQFASRKQLSDAILKEAEEKIKEELKAQGKPEKIWDNIIPGKMNSFIADNSQLDSKLTLMGQFYVMDDKKTVEQVIAEKEKEFGGKIKIVEFICFEVGEGLEKKTEDFAAEVAAQL",readFastaFile.get(0));
        assertEquals("SATVSEINSETDFVAKNDQFIALTKDTTAHIQSNSLQSVEELHSSTINGVKFEEYLKSQIATIGENLVVRRFATLKAGANGVVNGYIHTNGRVGVVIAAACDSAEVASKSRDLLRQICMH",readFastaFile.get(1));
    }

    @Test
    public void readFastaFileTest2() {
        GibbsSampler mf = new GibbsSampler();
        String sequencesPath = "resources/test/_2.0_8_500_10/sequences.fa";
        List<String> sequences = mf.readFastaFile(sequencesPath);

        assertEquals(10,sequences.size());

        String thirdSequence = "ACCGCAGATCTTAACGGTAATCGCCCATCATGCAGCTTAGAGAACGTCGTAGTAAGTCTGTGTGCTTGGTGGTGGAATAT" +
                "TCTATTAATTTCAGTGCAGTTCGCTTTTACACATAGTATTCCGCTTAGAAAAAGATGTCCTTCTTGACCGGCCGGGACAC" +
                "CGATTCTTAATAAGGGCCGCGCTCTGGACGAGGATGTTAGAGTACTGTCTGTCGACGCCCCGTTAGTAGCTACGCCTGTC" +
                "TGAGGACAGGTGAAGAAGCCACACTTTATGGTTGTCAGAACACCAATACCTGAAGCGCCAGTCACTCAGAGACACCACCC" +
                "GACGGGCCAAACGCGACCTTTCATTTCGGGAGCATTGCGGGGCCTGGATAACCGCTTATCTCGCCCCCACGGCCAAAGGG" +
                "CGCTAAAGATAAGTGTCTGCTCAACGTAGATGTCTGCCTGCTAGTCATTACCCGCAATGGGACAACGTAATTATACCGAA" +
                "GAAATTCGAGCATAAAGATT";
        assertEquals(thirdSequence, sequences.get(2));

        String eigthSequence = "TGAACTGCCCAACTTGACTCTCGGCATCCCGAACTCGCATTGATACATGACCATGGCTGAGTTAGCGTTCCAGCTCCCCC" +
                "ATCTGGCCGTCCATCTGATGCAATCCTCGGTAACCCTCACCTACCCGTGGAAGCCCTATGGGCTCTCTAATGAGCAGTTT" +
                "CAAGATGCAACGGATTAGGGCCGTATTAGTGCGCGACCTTACGCATTTTGGAAGGTTTACAAGGGGTTACCTTCGCTTCG" +
                "GTTCATTTAACATCAGGTTGCGAGGGGAGTGATTCTTACCCTCAGATCTTGTAACTTCGCCTATCACTCAGGAAAATCGC" +
                "GTGTCGACACCATCTTCCCTCCAGCTAGCGATCTCCTATATCAATCGATCCCCTCGCACACGCCAACACAGGATTTTGGG" +
                "ACGGCTGGGGCGTTCCAGTTGGGACTACACCAATCGCGTCGCCAAAGCCCCGATCTCTTCATTGAAGGACGCGATACAGG" +
                "GGAGTCTGGATCCTGGCTGG";
        assertEquals(eigthSequence, sequences.get(7));
    }

    @Test
    public void chooseMotifSitesTest1() {
        GibbsSampler mf = new GibbsSampler();
        List<String> readFAFile = new ArrayList<String>();
        readFAFile.add("A");
        readFAFile.add("AB");
        readFAFile.add("ABC");
        readFAFile.add("ABCABCABCABCABC"); // length = 15
        List<Integer> sites = mf.chooseMotifSites(readFAFile,2);
        assertTrue( sites.get(0) == -1 );
        assertTrue( sites.get(1) == 0);
        assertTrue( sites.get(2) <= 1 );
        assertTrue( sites.get(3) <= 13 );
    }
    @Test
    public void getPMTest1() {
        GibbsSampler mf = new GibbsSampler();
        List<String> seq = new ArrayList<String>();
        seq.add("ATTGA");
        seq.add("TGGTG");
        seq.add("ATCGG");
        seq.add("GCCCC");
        seq.add("CCCAA");
        seq.add("GACCG");
        seq.add("GCCCT");
        List<List<Integer>> pm = mf.getPM(seq);
        assertEquals( 2, (int)pm.get(0).get(0)); // pos = 0 'A' = 2
        assertEquals( 1, (int)pm.get(0).get(1)); // pos = 0 'C' = 1
        assertEquals( 1, (int)pm.get(0).get(2)); // pos = 0 'T' = 1
        assertEquals( 3, (int)pm.get(0).get(3)); // pos = 0 'G' = 3
        assertEquals( 1, (int)pm.get(3).get(0)); // pos = 3 'A' = 1
    }
    @Test
    public void pm2PWMTest1() {
        GibbsSampler mf = new GibbsSampler();
        List<String> seq = new ArrayList<String>();
        seq.add("AGAG");
        seq.add("TGAG");
        seq.add("CGAA");
        seq.add("GCAA");
        List<List<Integer>> pm = mf.getPM(seq);
        List<List<Double>> pwm = mf.pm2PWM(pm);
        assertTrue( 0.25 == pwm.get(0).get(0)); // pos = 0 'A'
        assertTrue( 0.25 == pwm.get(0).get(1)); // pos = 0 'C'
        assertTrue( 0.25 == pwm.get(0).get(2)); // pos = 0 'T'
        assertTrue( 0.25 == pwm.get(0).get(3)); // pos = 0 'G'

        assertTrue( 0.0+0.001   >   pwm.get(1).get(0)); // pos = 0 'A'
        assertTrue( 0.25-0.001  <   pwm.get(1).get(1)); // pos = 0 'C'
        assertTrue( 0.0+0.001   >   pwm.get(1).get(2)); // pos = 0 'T'
        assertTrue( 0.75-0.001  <   pwm.get(1).get(3)); // pos = 0 'G'

        assertTrue( 1-0.001     <   pwm.get(2).get(0)); // pos = 0 'A'
        assertTrue( 0.0+0.001   >   pwm.get(2).get(1)); // pos = 0 'C'
        assertTrue( 0.0+0.001   >   pwm.get(2).get(2)); // pos = 0 'T'
        assertTrue( 0.0+0.001   >   pwm.get(2).get(3)); // pos = 0 'G'

        assertTrue( 0.5-0.001   <   pwm.get(3).get(0)); // pos = 0 'A'
        assertTrue( 0.0+0.001   >   pwm.get(3).get(1)); // pos = 0 'C'
        assertTrue( 0.0+0.001   >   pwm.get(3).get(2)); // pos = 0 'T'
        assertTrue( 0.5-0.001   <   pwm.get(3).get(3)); // pos = 0 'G'
    }
    @Test
    public void genLogProbbyTTest1() {
        GibbsSampler mf = new GibbsSampler();
        List<String> seq = new ArrayList<String>();
        seq.add("AAAA");
        seq.add("AAAA");
        seq.add("TTTT");
        seq.add("CCCC");
        seq.add("GGGG");
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(0);
        a.add(0);
        a.add(0);
        a.add(0);
        a.add(0);
        assertTrue( Math.log(0.25)*4 == mf.genLogProbbyT( seq ,0,0,a, 4));
    }


    @Test
    public void findTest() {
        List<String> seq = generateRandomSequences(5,20);
        String motif = "GGGGAGGGG";
        Random rand = new Random(System.currentTimeMillis());
        List<Integer> correct = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            int ran = rand.nextInt( 20 );
            correct.add(ran);
            seq.set( i, seq.get(i).substring(0,ran).concat(motif).concat( seq.get(i).substring(ran) ) );
        }
        System.out.println("============= Correct Answer=============");
        System.out.println(correct);

        GibbsSampler gs = new GibbsSampler();
        gs.setSequences(seq);
        gs.setMotifLength(motif.length());
        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
        assertTrue(correct.containsAll( gs.gibbsSamplingfinder(15,"./")));
    }
}