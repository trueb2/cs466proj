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
        ArrayList<String> sequences = generateRandomSequences(sc, sl);
        String motif = generateMotif(icpc, ml);
        ArrayList<Integer> bindingSites = generateBindingSites(sequences, motif);
        ArrayList<Pair<String,Integer>> plantedSequences = plantMotifInSequences(bindingSites, sequences);
        writeFasta(Utils.getSequenceFromPair(plantedSequences), fastaFileName);
        writeSites(Utils.getSiteFromPair(plantedSequences), sitesFileName);
        writeMotif(motif, motifFileName);
        writeMotifLength(ml, motifLengthFileName);
    }

    /**
     * Generate Sequence Count random sequences
     * with uniform nucleotide frequencies
     * Each random sequence has length Sequence Length
     * @param sc, Sequence Count
     * @param sl, Sequence Length
     * @return generateSequences
     */
    public static ArrayList<String> generateRandomSequences(int sc, int sl) {
        ArrayList<String> randomSequences = new ArrayList<>(sc);
        for(int i = 0; i < sc; i++) {
            randomSequences.add(Utils.randomBases(sl));
        }
        return randomSequences;
    }

    /**
     * Generate a random motif, positive weight matrix of length Motif Length
     * with total information content being
     * Information Content Per Column * Motif Length
     * @param icpc, Information Content Per Column
     * @param ml, Motif Length
     * @return motif
     */
    public static String generateMotif(double icpc, int ml) {
        //TODO: IMPLEMENT THIS
        return Utils.randomBases(ml);
    }

    /**
     * Generate binding sites that maximize the information content of the
     * resulting PWM of the sequences
     * @param sequences, Sequence on which to calculate the binding sites
     * @param motif, the motif that we will plant
     * @return bindingSites
     */
    public static ArrayList<Integer> generateBindingSites(ArrayList<String> sequences, String motif) {
        return Utils.randomBindingSites(sequences.get(0).length(), motif.length());
    }

    /**
     * Plants one sampled site at a random location in each sequence.
     * Planting a site means overwriting the substring with the site.
     * @param sites
     * @param sequences
     * @return plantedSequences, list of pairs of the planted sequence and location of plant
     */
    public static ArrayList<Pair<String,Integer>> plantMotifInSequences(ArrayList<Integer> sites, ArrayList<String> sequences) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Writes the sequences in FASTA format to a file with the given name
     * @param sequences
     * @param filename
     * @return success of writing to file
     */
    public static boolean writeFasta(ArrayList<String> sequences, String filename) {
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
    public static void writeMotif(String motif, String filename) {
    }

    /**
     * Write down the motif length
     * @param ml
     * @param filename
     */
    public static void writeMotifLength(int ml, String filename) {
        //TODO: IMPLEMENT THIS
    }
}
