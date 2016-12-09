package edu.illinois.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeightMatrix {
    int rowSum = 1000000;
    int[][] countsMatrix;

    WeightMatrix(List<List<Integer>> countsLists) {
        int[][] countMatrix = listsToArrays(countsLists);
        setCountsMatrix(countMatrix);
    }

    WeightMatrix() {}

    private int[][] listsToArrays(List<List<Integer>> countsLists) {
        int[][] countMatrix = new int[countsLists.size()][countsLists.get(0).size()];
        List<int[]> rows = countsLists.stream()
                .map(r -> r.stream().mapToInt(Integer::intValue).toArray())
                .collect(Collectors.toList());
        IntStream.range(0,countsLists.size()).forEach(i -> countMatrix[i] = rows.get(i));
        return countMatrix;
    }

    void initMatrix(int rows) {
        countsMatrix = new int[rows][4];
    }

    private void setCountsMatrix(int[][] countsMatrix) {
        List<Integer> sumByRow = IntStream.range(0, countsMatrix.length).mapToObj(i -> {
            assert(countsMatrix[i].length == 4);
            return Arrays.stream(countsMatrix[i]).sum();
        }).collect(Collectors.toList());

        Integer sum = sumByRow.get(0);
        boolean allMatch = sumByRow.stream().allMatch(sum::equals);
        assert(allMatch);
        this.countsMatrix = countsMatrix;
        this.rowSum = sum;
    }

    @Override
    public String toString() {
        return Arrays.stream(countsMatrix)
                .map(b -> String.format("%d %d %d %d", b[0], b[1], b[2], b[3]))
                .collect(Collectors.joining("\n"));
    }
}
