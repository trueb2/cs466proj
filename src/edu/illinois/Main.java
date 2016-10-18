package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        //information content per column
        double icpc = Double.parseDouble(args[0]);
        //motif length
        int ml = Integer.parseInt(args[1]);
        //sequence length
        int sl = Integer.parseInt(args[2]);
        //sequence count
        int sc = Integer.parseInt(args[3]);

        ArrayList<String> sequences = generateRandomSequences(sc, sl);
        String motif = generateRandomMotif(icpc, ml);
        ArrayList<String> bindingSites = generateBindingSites(sc, motif);
        ArrayList<Pair<String,Integer>> plantedSequences = plantMotifInSequences(bindingSites, sequences);
        writeFasta(getSequenceFromPair(plantedSequences), "sequences.fa");
        writeSites(getSiteFromPair(plantedSequences), "sites.txt");
        writeMotif(motif, "motif.txt");
        writeMotifLength(ml, "motiflength.txt");
    }

    private static ArrayList<String> getSequenceFromPair(ArrayList<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<Integer> getSiteFromPair(ArrayList<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getValue())
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Generate Sequence Count random sequences
     * with uniform nucleotide frequencies
     * Each random sequence has length Sequence Length
     * @param sc, Sequence Count
     * @param sl, Sequence Length
     * @return generateSequences
     */
    private static ArrayList<String> generateRandomSequences(int sc, int sl) {
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
    private static String generateRandomMotif(double icpc, int ml) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Generate Sequence Count binding sites by sampling from the given motif
     * @param sc, Sequence Count
     * @param motif
     * @return bindingSites
     */
    private static ArrayList<String> generateBindingSites(int sc, String motif) {
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
    private static ArrayList<Pair<String,Integer>> plantMotifInSequences(ArrayList<String> sites, ArrayList<String> sequences) {
        //TODO: IMPLEMENT THIS
        return null;
    }

    /**
     * Writes the sequences in FASTA format to a file with the given name
     * @param sequences
     * @param filename
     * @return success of writing to file
     */
    private static boolean writeFasta(ArrayList<String> sequences, String filename) {
        //TODO: IMPLEMENT THIS
        return false;
    }

    /**
     * Use any format for writing down the location of the planted site in each sequence
     * @param sites
     * @param filename
     */
    private static void writeSites(ArrayList<Integer> sites, String filename) {
        //TODO: IMPLEMENT THIS
    }

    /**
     * Write down the motif that was generated. It should be stored in a format as
     * shown in the miniproj.pdf in step 8
     * @param motif
     * @param filename
     */
    private static void writeMotif(String motif, String filename) {
    }

    /**
     * Write down the motif length
     * @param ml
     * @param filename
     */
    private static void writeMotifLength(int ml, String filename) {
        //TODO: IMPLEMENT THIS
    }
}
