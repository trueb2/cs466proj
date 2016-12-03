package edu.illinois;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
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
        writeSequenceInfo(icpc, ml, sl, sc,
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
     * Writes all of the files that need to be written
     * @param icpc, information content per column
     * @param ml
     * @param sl
     * @param sc
     * @param fastaFileName
     * @param sitesFileName
     * @param motifFileName
     * @param motifLengthFileName
     * @param motif
     * @param bindingSites
     * @param plantedSequences
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void writeSequenceInfo(double icpc, int ml, int sl, int sc, String fastaFileName, String sitesFileName, String motifFileName, String motifLengthFileName, WeightMatrix motif, List<String> bindingSites, List<Pair<String, Integer>> plantedSequences) throws FileNotFoundException, UnsupportedEncodingException {
        //Create the output directory
        initOutputDirectory(fastaFileName);

        //Write files in the new directory
        writeFasta(sc, Utils.getSequenceFromPair(plantedSequences), fastaFileName);
        writeSites(sc, bindingSites, Utils.getSiteFromPair(plantedSequences), sitesFileName);
        writeMotif(ml, motif,motifFileName);
        writeMotifLength(ml, motifLengthFileName);
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

    /**
     * Writes the sequences in FASTA format to a file with the given name
     * @param sequences
     * @param filename
     * @return success of writing to file
     */
    public static void writeFasta(int sc, List<String> sequences, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        range(0,sc).forEach(i -> {
            String sequence = sequences.get(i);
            StringBuilder stringBuilder = new StringBuilder(sequence);
            range(0,sequence.length()/80).forEach(j -> stringBuilder.insert(j+(80*(j+1)),"\n"));
            stringBuilder.insert(0,String.format(">sequence%d\n",i));
            printWriter.println(stringBuilder.toString());
        });
        printWriter.close();
    }

    /**
     * Use any format for writing down the location of the planted site in each sequence
     * @param sites
     * @param siteFromPair
     * @param filename
     */
    static void writeSites(int sc, List<String> sites, ArrayList<Integer> siteFromPair, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        final PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        range(0,sc).forEach(i -> printWriter.println(String.format("%s %d", sites.get(i), siteFromPair.get(i))));
        printWriter.close();
    }

    /**
     * Write down the motif that was generated. It should be stored in a format as
     * shown in the miniproj.pdf in step 8
     * @param motif
     * @param filename
     */
    public static void writeMotif(int ml, WeightMatrix motif, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        printWriter.println(String.format(">%s\t%d", filename, ml));
        printWriter.println(motif); //not exactly right for debugging purposes
        printWriter.println("<");
        printWriter.close();
    }

    /**
     * Write down the motif length
     * @param ml
     * @param filename
     */
    public static void writeMotifLength(int ml, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        printWriter.println(ml);
        printWriter.close();
    }
}
