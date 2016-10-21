package edu.illinois;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by jwtrueb on 10/21/16.
 */
public class Utils {
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
}
