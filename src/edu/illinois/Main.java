package edu.illinois;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        //information content per column
        double icpc = Double.parseDouble(args[0]);
        //motif length
        int ml = Integer.parseInt(args[1]);
        //sequence length
        int sl = Integer.parseInt(args[2]);
        //sequence count
        int sc = Integer.parseInt(args[3]);

        //output file locations
        String outDir = "out/data/";
        String sequenceFileName = outDir + "sequence";
        String sitesFileName = outDir + "sites.txt";
        String motifFileName = outDir + "motif.txt";
        String motifLengthFileName = outDir + "motiflength.txt";

        //init directory for output
        initOutDir(motifFileName);

        SequenceGenerator.createAndWrite(icpc, ml, sl, sc,
                sequenceFileName,
                sitesFileName,
                motifFileName,
                motifLengthFileName);
    }

    private static void initOutDir(String motifFileName) {
        File motifFile = new File(motifFileName);
        File parentDir = motifFile.getParentFile();
        if(parentDir != null)
            parentDir.mkdirs();
    }

}
