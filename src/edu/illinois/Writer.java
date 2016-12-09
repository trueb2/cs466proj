package edu.illinois;

import edu.illinois.Matrix.WeightMatrix;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.IntStream.range;

public class Writer {
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
        writeMotif(ml, motif,motifFileName,icpc);
        writeMotifLength(ml, motifLengthFileName);
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
     * @param motifs
     * @param sitePositions
     * @param filename
     */
    public static void writeSites(int sc, List<String> motifs, List<Integer> sitePositions, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        final PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        range(0,sc).forEach(i -> printWriter.println(String.format("%s %d", motifs.get(i), sitePositions.get(i))));
        printWriter.close();
    }

    /**
     * Write down the motif that was generated. It should be stored in a format as
     * shown in the miniproj.pdf in step 8
     * @param motif
     * @param filename
     */
    public static void writeMotif(int ml, WeightMatrix motif, String filename, double icpc) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        printWriter.println(String.format(">%s_%f\t%d", filename, icpc, ml));
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

    /**
     * Creates the directory where the files will be written
     * @param filename, path to a file that needs to be written
     */
    static void initOutputDirectory(String filename) {
        File file = new File(filename);
        File parentDir = file.getParentFile();
        if(parentDir != null)
            parentDir.mkdirs();
    }
}
