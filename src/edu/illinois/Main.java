package edu.illinois;

import edu.illinois.benchmarks.OverlapBenchmark;
import edu.illinois.benchmarks.PrintSitesBenchmark;
import edu.illinois.benchmarks.RelativeEntropyBenchmark;
import edu.illinois.finders.GibbsSampler;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Main {
/**
 * Runtime Seq Gen = O( sc(sl+ml)+ml+sc )
 * Runtime GS = O( [ sc(sl+ml) ] + [ ns * mI * sc(ml+sl) ] )
 *      worst case : runs in sequential although we did it in parallel
 * Runtime B = O ( sc(sl+ml) )
**/
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if(args.length == 0) {
            //default
            Double icpc = 2.0;
            Integer ml = 8;
            Integer sc = 10;
            Integer sl = 500;

            args = new String[]{icpc.toString(), ml.toString(), sl.toString(), sc.toString()};
            run(args);
        } else if(args.length == 1) {
            //all default values
            double[] icpcDefaults = {1.0, 1.5, 2};
            int[] mlDefaults = {6, 7, 8};
            int[] scDefaults = {5, 10, 20};

            for(Double icpc : icpcDefaults)
                run(new String[]{icpc.toString(),"8","500","10"});
            for(Integer ml : mlDefaults)
                run(new String[]{"2.0",ml.toString(),"500","10"});
            for(Integer sc : scDefaults)
                run(new String[]{"2.0","8","500",sc.toString()});
        } else if (args.length == 2) {
            //all values that we want to test
            double[] icpcDefaults = {0.0, 1.0, 1.5, 2};
            int[] mlDefaults = {6, 7, 8, 16};
            int[] slDefaults = {500};
            int[] scDefaults = {5, 10, 20, 50};

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

    private static void run(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
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
                new Random(),
                outDir + fastaFileName,
                outDir + sitesFileName,
                outDir + motifFileName,
                outDir + motifLengthFileName);

        //perform motif finding
        GibbsSampler gibbsSampler = new GibbsSampler(fastaFileName, motifLengthFileName, outDir);
        gibbsSampler.find();

        //run benchmarks
        PrintSitesBenchmark.printSitesBenchmark(outDir);
        OverlapBenchmark.overlapBenchmark(outDir);
        RelativeEntropyBenchmark.relativeEntropyBenchmark(outDir);

    }
}
