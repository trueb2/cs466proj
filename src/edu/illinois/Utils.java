package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class Utils {
    static final String[] BASES = { "A", "C", "G", "T" };

    static ArrayList<String> getSequenceFromPair(ArrayList<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static ArrayList<Integer> getSiteFromPair(ArrayList<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(x -> x.getValue())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static String randomBases(int len) {
        Random r = new Random(System.currentTimeMillis());
        StringBuilder sequence = new StringBuilder(len);
        for(int j = 0; j < len; j++) {
            sequence.append(BASES[r.nextInt(4)]);
        }
        return sequence.toString();

    }

    static ArrayList<Integer> randomBindingSites(int sc, int ml) {
        Random r = new Random();
        int l = sc - ml;
        ArrayList<Integer> bindingSites = new ArrayList<>(sc);
        IntStream.range(0,l).forEach((x) -> bindingSites.add(r.nextInt(l)));
        return bindingSites;
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

    static int indexOfBase(char base) {
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

    public static int[] without(int[] with, int exclude) {
        return IntStream.range(0, with.length)
                .filter(i -> i != exclude)
                .map(i -> with[i])
                .toArray();
    }
}
