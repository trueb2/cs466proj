package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Utils {
    public static final String[] ACGT = { "A", "C", "G", "T" };
    private static final double LOG_2 = Math.log(2);

    static List<String> getSequenceFromPair(List<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static ArrayList<Integer> getSiteFromPair(List<Pair<String, Integer>> sequences) {
        return sequences
                .stream()
                .map(Pair::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String randomBases(int len, Random r) {
        return IntStream.range(0,len).mapToObj(i -> ACGT[r.nextInt(4)]).collect(Collectors.joining());
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
        DoubleStream probabilities = Arrays.stream(counts).mapToDouble(i -> ((double)i)/countSum);
        return probabilities.map(p -> {
            if(p == 0)
                return 0;
            else
                return p * (Math.log(p/.25)/ LOG_2);
        }).sum();
    }
}
