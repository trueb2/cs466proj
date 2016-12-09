package edu.illinois.benchmarks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by muzam on 12/3/2016.
 */
public class OverlapBenchmark extends Benchmark {

    String odr;
    int motifLength;

    public OverlapBenchmark(String outputDirectory, int motifLength) throws FileNotFoundException {
        super(outputDirectory);
        odr = outputDirectory;
        this.motifLength = motifLength;
    }

    @Override
    public void benchmark() {
        int numMotifOverlaps = 0;
        int numOverlappingPositions = 0;

        try {
            Scanner sitesScan = new Scanner(new FileReader(odr + "sites.txt"));
            Scanner predScan = new Scanner(new FileReader(odr + "predictedsites.txt"));

            while (sitesScan.hasNextLine() && predScan.hasNextLine()) {
                String sitesLine = sitesScan.nextLine();
                String siteSeq = sitesLine.substring(0,motifLength);
                int sitePos = Integer.parseInt(sitesLine.substring(motifLength+1));

                String predLine = predScan.nextLine();
                String predSeq = predLine.substring(0,motifLength);
                int predPos = Integer.parseInt(predLine.substring(motifLength+1));

                int diff = Math.abs(sitePos-predPos);

                if (doesOverlap(sitePos, predPos)) {
                    numMotifOverlaps++;
                    if (sitePos == predPos) {
                        numOverlappingPositions += overlapCount(siteSeq, predSeq);
                    }
                    else if (sitePos < predPos) {
                        numOverlappingPositions +=
                                overlapCount(siteSeq.substring(diff), predSeq.substring(0, (motifLength-diff)));
                    }
                    else if (sitePos > predPos) {
                        numOverlappingPositions +=
                                overlapCount(siteSeq.substring(0, (motifLength-diff)), predSeq.substring(diff));
                    }
                }
            }

            sitesScan.close();
            predScan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Number of overlapping motifs: " + numMotifOverlaps);
        System.out.println("Number of matched bases: " + numOverlappingPositions);
    }

    private boolean doesOverlap(int sitePos, int predPos) {
        return predPos < (sitePos + (motifLength-1)) && predPos > (sitePos - (motifLength-1));
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
