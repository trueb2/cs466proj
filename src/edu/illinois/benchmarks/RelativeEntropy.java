package edu.illinois.benchmarks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by muzam on 12/5/2016.
 */
public class RelativeEntropy extends Benchmark {

    String odr;
    int motifLength;

    public RelativeEntropy(String outputDirectory, int motifLength) {
        super(outputDirectory);
        odr = outputDirectory;
        this.motifLength = motifLength;
    }

    @Override
    public void benchmark() {
        double[] posRelEntropy = new double[motifLength];
        double[] accBaseProbs = new double[4];
        double[] predBaseProbs = new double[4];
        int[] accBaseFreqs = new int[4];
        int[] predBaseFreqs = new int[4];
        try {
            Scanner accMotifScan = new Scanner(new FileReader(odr + "motif.txt"));
            Scanner predMotifScan = new Scanner(new FileReader(odr + "predictedmotif.txt"));
            String accHolder = accMotifScan.nextLine();
            String predHolder = predMotifScan.nextLine();
            for (int i = 0; i < motifLength; i++) {
                int accBaseTotal = 0;
                int predBaseTotal = 0;
                for (int b = 0; b < 4; b++) {
                    accBaseFreqs[b] = accMotifScan.nextInt();
                    accBaseTotal += accBaseFreqs[b];
                    predBaseFreqs[b] = predMotifScan.nextInt();
                    predBaseTotal += predBaseFreqs[b];
                }
                double sum = 0;
                for (int b = 0; b < 4; b++) {
                    accBaseProbs[b] = (double) accBaseFreqs[b]/accBaseTotal;
                    predBaseProbs[b] = (double) predBaseFreqs[b]/predBaseTotal;
                    if (accBaseProbs[b] == 0 || predBaseProbs[b] == 0) {
                        sum += 0;
                    }
                    else {
                        sum += (accBaseProbs[b] * Math.log(accBaseProbs[b] / predBaseProbs[b]));
                    }
                }
                posRelEntropy[i] = sum;

            }

            accMotifScan.close();
            predMotifScan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Posiitonal relative entropy values = ");
        printDoubles(posRelEntropy);
        System.out.println();
        System.out.println("Total relative entropy D(acc|predicted) = ");
        sumDoubles(posRelEntropy);

    }

    private void printDoubles(double[] list) {
        for (double tmp : list) {
            System.out.print(tmp + " ");
        }
    }

    private void sumDoubles(double[] list) {
        double total = 0;
        for (double tmp : list) {
            total += tmp;
        }
        System.out.println(total);
    }
}
