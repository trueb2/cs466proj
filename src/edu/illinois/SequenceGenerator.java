package edu.illinois;

import javafx.util.Pair;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class SequenceGenerator {

    public static void createAndWrite(double icpc, int ml, int sl, int sc,
                               String fastaFileName,
                               String sitesFileName,
                               String motifFileName,
                               String motifLengthFileName) throws FileNotFoundException, UnsupportedEncodingException {
        List<String> sequences = generateRandomSequences(sc, sl);
        WeightMatrix motif = generateMotif(icpc, ml);
        List<String> bindingSites = generateBindingSites(sc, motif);
        List<Pair<String,Integer>> plantedSequences = plantMotifInSequences(sc, sl, ml, bindingSites, sequences);
        Writer.writeSequenceInfo(icpc, ml, sl, sc,
                fastaFileName,
                sitesFileName,
                motifFileName,
                motifLengthFileName,
                motif,
                bindingSites,
                plantedSequences);
    }


    /**
     * Generate Sequence Count random sequences
     * with uniform nucleotide frequencies
     * Each random sequence has length Sequence Length
     * @param sc, Sequence Count
     * @param sl, Sequence Length
     * @return generateSequences
     */
    public static List<String> generateRandomSequences(int sc, int sl) {
        Random r = new Random();
        return range(0,sc)
                .mapToObj(i -> Utils.randomBases(sl, r))
                .collect(Collectors.toList());
    }

    /**
     * Generate a random motif, positive weight matrix of length Motif Length
     * with total information content being
     * Information Content Per Column * Motif Length
     * @param icpc, Information Content Per Column
     * @param ml, Motif Length
     * @return motif
     */
    public static WeightMatrix generateMotif(double icpc, int ml) {
        return new WeightMatrix(icpc, ml);
    }

    /**
     * Generate Strings to plant using the weights in the motif
     * @param motif, the motif that we will plant
     * @return bindingSites
     */
    public static List<String> generateBindingSites(int sc, WeightMatrix motif) {
        Random r = new Random();
        return range(0,sc).mapToObj(i -> motif.sample(r)).collect(Collectors.toList());
    }

    /**
     * Plants one sampled site at a random location in each sequence.
     * Planting a site means overwriting the substring with the site.
     * @param sites
     * @param sequences
     * @return plantedSequences, list of pairs of the planted sequence and location of plant
     */
    public static List<Pair<String,Integer>> plantMotifInSequences(int sc, int sl, int ml, List<String> sites, List<String> sequences) {
        Random r = new Random();
        int bound = sl - ml;
        return range(0,sc).mapToObj(i -> {
            String sequence = sequences.get(i);
            int idx = r.nextInt(bound);
            String start = sequence.substring(0,idx);
            String end = sequence.substring(idx+ml);
            return new Pair<>(start + sites.get(i) + end, idx);
        }).collect(Collectors.toList());
    }

    /**
     * Creates the directory where the files will be written
     * @param filename, path to a file that needs to be written
     */
    private static void initOutputDirectory(String filename) {
        File file = new File(filename);
        File parentDir = file.getParentFile();
        if(parentDir != null)
            parentDir.mkdirs();
    }

}
