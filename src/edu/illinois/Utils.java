package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class Utils {
    public static final String[] ACGT = { "A", "C", "G", "T" };

    static List<String> getSequenceFromPair(List<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static ArrayList<Integer> getSiteFromPair(List<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getValue())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String randomBases(int len, Random r) {
        return IntStream.range(0,len).mapToObj(i -> ACGT[r.nextInt(4)]).collect(Collectors.joining());
    }

    static double[][] calculatePWM(ArrayList<String> sequences, int ignoredSequence) {
        int sc = sequences.size();
        int sl = sequences.get(0).length();
        double[][] pwm = new double[4][sl];

        //count the number of occurrences
        for(int i = 0; i < sc; i++) {
            if(i == ignoredSequence)
                continue;
            String sequence = sequences.get(i);
            for(int j = 0; j < sl; j++) {
                int baseIndex = indexOfBase(sequence.charAt(j));
                pwm[baseIndex][j] = pwm[baseIndex][j] + 1;
            }
        }

        //divide by the number of sequences that were used
        double s = sc - 1;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < sl; j++) {
                pwm[i][j] = pwm[i][j] / s;
            }
        }

        return pwm;
    }

    static int weightedRandomIndex(ArrayList<Double> weights) {
        double total = weights.stream().reduce(Double::sum).get();
        double random = Math.random() * total;
        for(int i = 0; i < weights.size(); i++) {
            random -= weights.get(i);
            if(random < 0.0)
                return i;
        }
        return weights.size() - 1;
    }

    public static int indexOfBase(char base) {
        switch(base) {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
            default:
                System.out.println("Unknown Base");
                return -1;
        }
    }

    public static double calcInformationContent(double countSum, int[] counts) {
        double log_2 = Math.log(2);
        DoubleStream probabilities = Arrays.stream(counts).mapToDouble(i -> ((double)i)/countSum);
        return probabilities.map(p -> {
            if(p == 0)
                return 0;
            else
                return p * (Math.log(p/.25)/log_2);
        }).sum();
    }
}
