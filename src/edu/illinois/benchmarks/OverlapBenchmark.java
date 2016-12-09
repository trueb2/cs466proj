package edu.illinois.benchmarks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class OverlapBenchmark extends Benchmark {

    private OverlapBenchmark(String outputDirectory) throws FileNotFoundException {
        super(outputDirectory, "OverlapBenchmark");
    }

    public static void overlapBenchmark(String outputDirectory) {
        try {
            OverlapBenchmark ob = new OverlapBenchmark(outputDirectory);
            ob.benchmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void benchmark() {
        int numMotifOverlaps = 0;
        int numOverlappingPositions = 0;

        try {
            Scanner sitesScan = new Scanner(new FileReader(outputDirectory + "sites.txt"));
            Scanner predScan = new Scanner(new FileReader(outputDirectory + "predictedsites.txt"));

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
        System.out.println();
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
