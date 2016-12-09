package edu.illinois.finders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class MotifFinder {
    public List<String> getSequences() {
        return sequences;
    }

    public int getMotifLength() {
        return motifLength;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public int getSequenceCount() {
        return sequenceCount;
    }

    int motifLength;
    int sequenceLength;
    int sequenceCount;
    String outputDirectory;
    List<String> sequences;

    public MotifFinder(String fastaFileName, String motifLengthFileName,  String outputDirectory) {
        sequences = readFastaFile(fastaFileName);
        motifLength = readMotifLengthFile(motifLengthFileName);
        sequenceLength = sequences.get(0).length();
        sequenceCount = sequences.size();
        this.outputDirectory = outputDirectory;
    }

    public MotifFinder() {}
    
    /**
     * Method implemented by all MotifFinders that
     * will perform the search for the motifs in the sequences
     */
    public abstract void find();
    
    /**
     * Load all sequences from *.fa
     * @param fileName path of fa file
     * @return Set of Gene Sequence String
     */
    public List<String> readFastaFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new FileReader(fileName));
            List<String> sequences = new ArrayList<>();
            StringBuilder sequenceBuilder = new StringBuilder();
            @SuppressWarnings("UnusedAssignment")
            String line = scanner.nextLine();
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if(line.charAt(0) != '>')
                    sequenceBuilder.append(line);
                else {
                    sequences.add(sequenceBuilder.toString());
                    sequenceBuilder = new StringBuilder(sequenceBuilder.length());
                }
            }
            sequences.add(sequenceBuilder.toString());
            scanner.close();
            return sequences;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Load size.txt
     * @param path path of size file
     * @return int of motif size
     */
    public int readMotifLengthFile(String path){
        try{
            Scanner scanner = new Scanner(new FileReader(path));
            int size = Integer.valueOf(scanner.nextLine());
            scanner.close();
            return size;
        }catch(IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public void setSequences(List<String> sequences) {
        this.sequences = sequences;
    }

    public void setMotifLength(int motifLength) {
        this.motifLength = motifLength;
    }
}
