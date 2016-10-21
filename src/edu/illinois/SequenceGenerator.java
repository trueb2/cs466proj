package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class SequenceGenerator {

    static void createAndWrite(double icpc, int ml, int sl, int sc,
                               String fastaFileName,
                               String sitesFileName,
                               String motifFileName,
                               String motifLengthFileName) {
        ArrayList<String> sequences = SequenceGenerator.generateRandomSequences(sc, sl);
        String motif = SequenceGenerator.generateRandomMotif(icpc, ml);
        ArrayList<String> bindingSites = SequenceGenerator.generateBindingSites(sc, motif);
        ArrayList<Pair<String,Integer>> plantedSequences = SequenceGenerator.plantMotifInSequences(bindingSites, sequences);
        SequenceGenerator.writeFasta(Utils.getSequenceFromPair(plantedSequences), fastaFileName);
        SequenceGenerator.writeSites(Utils.getSiteFromPair(plantedSequences), sitesFileName);
        SequenceGenerator.writeMotif(motif, motifFileName);
        SequenceGenerator.writeMotifLength(ml, motifLengthFileName);
    }

    /**
     * Generate Sequence Count random sequences
     * with uniform nucleotide frequencies
     * Each random sequence has length Sequence Length
     * @param sc, Sequence Count
     * @param sl, Sequence Length
     * @return generateSequences
     */
    static ArrayList<String> generateRandomSequences(int sc, int sl) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Generate a random motif, positive weight matrix of length Motif Length
     * with total information content being
     * Information Content Per Column * Motif Length
     * @param icpc, Information Content Per Column
     * @param ml, Motif Length
     * @return motif
     */
    static String generateRandomMotif(double icpc, int ml) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Generate Sequence Count binding sites by sampling from the given motif
     * @param sc, Sequence Count
     * @param motif
     * @return bindingSites
     */
    static ArrayList<String> generateBindingSites(int sc, String motif) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Plants one sampled site at a random location in each sequence.
     * Planting a site means overwriting the substring with the site.
     * @param sites
     * @param sequences
     * @return plantedSequences, list of pairs of the planted sequence and location of plant
     */
    static ArrayList<Pair<String,Integer>> plantMotifInSequences(ArrayList<String> sites, ArrayList<String> sequences) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Writes the sequences in FASTA format to a file with the given name
     * @param sequences
     * @param filename
     * @return success of writing to file
     */
    static boolean writeFasta(ArrayList<String> sequences, String filename) {
        //TODO: IMPLEMENT THIS
        return false;
    }

    /**
     * Use any format for writing down the location of the planted site in each sequence
     * @param sites
     * @param filename
     */
    static void writeSites(ArrayList<Integer> sites, String filename) {
        //TODO: IMPLEMENT THIS
    }

    /**
     * Write down the motif that was generated. It should be stored in a format as
     * shown in the miniproj.pdf in step 8
     * @param motif
     * @param filename
     */
    static void writeMotif(String motif, String filename) {
    }

    /**
     * Write down the motif length
     * @param ml
     * @param filename
     */
    static void writeMotifLength(int ml, String filename) {
        //TODO: IMPLEMENT THIS
    }
}
