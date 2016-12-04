package edu.illinois.benchmarks;

import edu.illinois.finders.MotifFinder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by muzam on 12/3/2016.
 */
public class Overlap_Benchmark extends Benchmark {

    String odr;
    int motifLength;

    public Overlap_Benchmark(String outputDirectory, int motifLength) {
        super(outputDirectory);
        odr = outputDirectory;
        this.motifLength = motifLength;
    }

    @Override
    public void benchmark() {
        int numMotifOverlaps = 0;
        int numOverlappingPositions = 0;

        try {
            Scanner sites_scan = new Scanner(new FileReader(odr + "sites.txt"));
            Scanner pred_scan = new Scanner(new FileReader(odr + "predictedsites.txt"));

            while (sites_scan.hasNextLine() && pred_scan.hasNextLine()) {
                String sites_line = sites_scan.nextLine();
                String site_seq = sites_line.substring(0,motifLength);
                int site_pos = Integer.parseInt(sites_line.substring(motifLength+1));

                String pred_line = pred_scan.nextLine();
                String pred_seq = pred_line.substring(0,motifLength);
                int pred_pos = Integer.parseInt(pred_line.substring(motifLength+1));

                int diff = Math.abs(site_pos-pred_pos);

                if (pred_pos < (site_pos + (motifLength-1)) && pred_pos > (site_pos - (motifLength-1))) {
                    numMotifOverlaps++;
                    if (site_pos == pred_pos) {
                        numOverlappingPositions += overlapCount(site_seq, pred_seq);
                    }
                    else if (site_pos < pred_pos) {
                        numOverlappingPositions +=
                                overlapCount(site_seq.substring(diff), pred_seq.substring(0, (motifLength-diff)));
                    }
                    else if (site_pos > pred_pos) {
                        numOverlappingPositions +=
                                overlapCount(site_seq.substring(0, (motifLength-diff)), pred_seq.substring(diff));
                }
                }
            }

            sites_scan.close();
            pred_scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Number of overlapping motifs: " + numMotifOverlaps);
        System.out.println("Number of matched bases: " + numOverlappingPositions);
    }

    private int overlapCount(String w1, String w2) {
        int counter = 0;
        char[] first  = w1.toLowerCase().toCharArray();
        char[] second = w2.toLowerCase().toCharArray();

        int minLength = w1.length();

        for(int i = 0; i < minLength; i++)
        {
            if (first[i] == second[i])
            {
                counter++;
            }
        }

        return counter;
    }
}
