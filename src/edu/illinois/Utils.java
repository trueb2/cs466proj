package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

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
}
