package edu.illinois;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if(args.length == 0) {
            //default
            Double icpc = 2.0;
            Integer ml = 8;
            Integer sc = 10;
            Integer sl = 500;

            args = new String[]{icpc.toString(), ml.toString(), sl.toString(), sc.toString()};
            run(args, true);
        } else if(args.length == 1) {
            //other default values
            double[] icpcDefaults = {1, 1.5, 2};
            int[] mlDefaults = {6, 7, 8};
            int[] slDefaults = {500};
            int[] scDefaults = {5, 10, 20};

            for(Double icpc : icpcDefaults) {
                for(Integer ml : mlDefaults) {
                    for(Integer sl : slDefaults) {
                        for(Integer sc : scDefaults) {
                            args = new String[]{icpc.toString(), ml.toString(), sl.toString(), sc.toString()};
                            run(args, false);
                        }
                    }
                }
            }
        } else {
            run(args, true);
        }
    }

    private static void run(String[] args, boolean defaultOut) throws FileNotFoundException, UnsupportedEncodingException {
        //information content per column
        double icpc = Double.parseDouble(args[0]);
        //motif length
        int ml = Integer.parseInt(args[1]);
        //sequence length
        int sl = Integer.parseInt(args[2]);
        //sequence count
        int sc = Integer.parseInt(args[3]);

        //output file locations
        String outDir = "out/data/seq" + (defaultOut ? "/" : "");
        String sequenceFileName = "sequences";
        String sitesFileName = "sites";
        String motifFileName = "motif";
        String motifLengthFileName = "motiflength";

        //do sequence generation and write out files containing the descriptions
        SequenceGenerator.createAndWrite(icpc, ml, sl, sc,
                outDir,
                sequenceFileName,
                sitesFileName,
                motifFileName,
                motifLengthFileName);
    }
}
