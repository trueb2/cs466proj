package edu.illinois.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.illinois.SequenceGenerator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class MotifFinderTest {

    @Test
    public void readFAFileTest1() {
        MotifFinder mf = new MotifFinder();
        mf.readFAFile(./test.fa);
        List<String> readFAFile = generateRandomSequences(2,10);
        assertEquals(readFAFile.size(),2);
        assertEquals(readFAFile.get(0),"MTEITAAMVKELRESTGAGMMDCKNALSETNGDFDKAVQLLREKGLGKAAKKADRLAAEGLVSVKVSDDFTIAAMRPSYLSYEDLDMTFVENEYKALVAELEKENEERRRLKDPNKPEHKIPQFASRKQLSDAILKEAEEKIKEELKAQGKPEKIWDNIIPGKMNSFIADNSQLDSKLTLMGQFYVMDDKKTVEQVIAEKEKEFGGKIKIVEFICFEVGEGLEKKTEDFAAEVAAQL");
        assertEquals(readFAFile.get(1),"SATVSEINSETDFVAKNDQFIALTKDTTAHIQSNSLQSVEELHSSTINGVKFEEYLKSQIATIGENLVVRRFATLKAGANGVVNGYIHTNGRVGVVIAAACDSAEVASKSRDLLRQICMH");
    }

//    @Test
//    public void generateRandomMotif1() {
//        String motif = generateMotif(1, 10);
//        assertEquals(10, motif.length());
//    }
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