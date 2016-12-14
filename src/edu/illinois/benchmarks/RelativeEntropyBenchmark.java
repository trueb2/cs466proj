package edu.illinois.benchmarks;

import edu.illinois.Writer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RelativeEntropyBenchmark extends Benchmark {
    
    private RelativeEntropyBenchmark(String outputDirectory) throws FileNotFoundException {
        super(outputDirectory, "RelativeEntropyBenchmark");
    }

    public static void relativeEntropyBenchmark(String outputDirectory) {
        try {
            RelativeEntropyBenchmark ob = new RelativeEntropyBenchmark(outputDirectory);
            ob.benchmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void benchmark() {
        double[] posRelEntropy = new double[motifLength];
        double[] accBaseProbs = new double[4];
        double[] predBaseProbs = new double[4];
        int[] accBaseFreqs = new int[4];
        int[] predBaseFreqs = new int[4];
        try {
            Scanner accMotifScan = new Scanner(new FileReader(outputDirectory + "motif.txt"));
            Scanner predMotifScan = new Scanner(new FileReader(outputDirectory + "predictedmotif.txt"));
            accMotifScan.nextLine();
            predMotifScan.nextLine();
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

        String entropiesString = Arrays.stream(posRelEntropy)
                .mapToObj(Double::toString)
                .collect(Collectors.joining( " "));
        String totalEntropyString = ((Double) Arrays.stream(posRelEntropy).sum()).toString();
        System.out.println(String.format("Positional relative entropy values = %s", entropiesString));
        System.out.println(String.format("Total relative entropy D(acc|predicted) = %s", totalEntropyString));

        try {
            Writer.writeResult(outputDirectory, BENCHMARK, entropiesString + "\n" + totalEntropyString);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
