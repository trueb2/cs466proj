package edu.illinois.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import edu.illinois.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class MotifFinderTest {

    @Test
    public void readFAFileTest1() {
        MotifFinder mf = new MotifFinder();
        List<String> readFAFile = mf.readFAFile("./test.fa");
        assertEquals(2,readFAFile.size());
        assertEquals("MTEITAAMVKELRESTGAGMMDCKNALSETNGDFDKAVQLLREKGLGKAAKKADRLAAEGLVSVKVSDDFTIAAMRPSYLSYEDLDMTFVENEYKALVAELEKENEERRRLKDPNKPEHKIPQFASRKQLSDAILKEAEEKIKEELKAQGKPEKIWDNIIPGKMNSFIADNSQLDSKLTLMGQFYVMDDKKTVEQVIAEKEKEFGGKIKIVEFICFEVGEGLEKKTEDFAAEVAAQL",readFAFile.get(0));
        assertEquals("SATVSEINSETDFVAKNDQFIALTKDTTAHIQSNSLQSVEELHSSTINGVKFEEYLKSQIATIGENLVVRRFATLKAGANGVVNGYIHTNGRVGVVIAAACDSAEVASKSRDLLRQICMH",readFAFile.get(1));
    }

    @Test
    public void chooseMotifSitesTest1() {
        MotifFinder mf = new MotifFinder();
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
//
//    @Test
//    public void generateBindingSites1() {
//        ArrayList<String> sequences = generateRandomSequences(10,20);
//        String motif = generateMotif(1,4);
//        ArrayList<Integer> bindingSites = generateBindingSites(sequences,motif);
//        assertTrue(bindingSites.stream().max(Integer::max).get() < 16);
//        assertTrue(bindingSites.stream().min(Integer::min).get() >= 0);
//    }
}