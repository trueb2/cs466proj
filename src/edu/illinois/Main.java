package edu.illinois;

import edu.illinois.benchmarks.OverlapBenchmark;
import edu.illinois.finders.GibbsSampler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if(args.length == 0) {
            //default
            Double icpc = 1.5;
            Integer ml = 8;
            Integer sc = 10;
            Integer sl = 500;

            args = new String[]{icpc.toString(), ml.toString(), sl.toString(), sc.toString()};
            run(args);
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
                            run(args);
                        }
                    }
                }
            }
        } else {
            run(args);
        }
    }

    public static void run(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        //information content per column
        double icpc = Double.parseDouble(args[0]);
        //motif length
        int ml = Integer.parseInt(args[1]);
        //sequence length
        int sl = Integer.parseInt(args[2]);
        //sequence count
        int sc = Integer.parseInt(args[3]);

        //output file locations
        String outDir = String.format("out/data/seq_%.1f_%d_%d_%d/", icpc, ml, sl, sc);
        String fastaFileName = "sequences.fa";
        String sitesFileName = "sites.txt";
        String motifFileName = "motif.txt";
        String motifLengthFileName = "motiflength.txt";

        //do sequence generation and write out files containing the descriptions
        SequenceGenerator.createAndWrite(icpc, ml, sl, sc,
                outDir + fastaFileName,
                outDir + sitesFileName,
                outDir + motifFileName,
                outDir + motifLengthFileName);

        //perform motif finding
        GibbsSampler gibbsSampler = new GibbsSampler(fastaFileName, motifLengthFileName, outDir);
        gibbsSampler.find();

        //run benchmarks
        OverlapBenchmark ob = new OverlapBenchmark(outDir, gibbsSampler.getMotifLength());
        ob.benchmark();
    }
}
