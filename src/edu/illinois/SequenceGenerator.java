package edu.illinois;

import edu.illinois.Matrix.MotifMatrix;
import javafx.util.Pair;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class SequenceGenerator {

    public static void createAndWrite(double icpc, int ml, int sl, int sc,
                               Random r,
                               String fastaFileName,
                               String sitesFileName,
                               String motifFileName,
                               String motifLengthFileName) throws FileNotFoundException, UnsupportedEncodingException {
        List<String> sequences = generateRandomSequences(sc, sl, r);
        MotifMatrix motif = generateMotif(icpc, ml);
        List<String> bindingSites = generateBindingSites(sc, r, motif);
        List<Pair<String,Integer>> plantedSequences = plantMotifInSequences(sc, sl, ml, r, bindingSites, sequences);
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
     * @param r, object that provides randomness
     * @return generateSequences
     */
    public static List<String> generateRandomSequences(int sc, int sl, Random r) {
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
    private static MotifMatrix generateMotif(double icpc, int ml) {
        return new MotifMatrix(icpc, ml);
    }

    /**
     * Generate Strings to plant using the weights in the motif
     * @param r, object that provides randomness
     * @param motif, the motif that we will plant
     * @return bindingSites
     */
    private static List<String> generateBindingSites(int sc, Random r, MotifMatrix motif) {
        return range(0,sc).mapToObj(i -> motif.sample(r)).collect(Collectors.toList());
    }

    /**
     * Plants one sampled site at a random location in each sequence.
     * Planting a site means overwriting the substring with the site.
     * @param r, object that provides randomness
     * @param sites, plants site values by overwriting
     * @param sequences, sequences to plant motifs in
     * @return plantedSequences, list of pairs of the planted sequence and location of plant
     */
    private static List<Pair<String,Integer>> plantMotifInSequences(int sc, int sl, int ml, Random r, List<String> sites, List<String> sequences) {
        int bound = sl - ml;
        return range(0,sc).mapToObj(i -> {
            String sequence = sequences.get(i);
            int idx = r.nextInt(bound);
            String start = sequence.substring(0,idx);
            String end = sequence.substring(idx+ml);
            return new Pair<>(start + sites.get(i) + end, idx);
        }).collect(Collectors.toList());
    }

}
