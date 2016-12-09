package edu.illinois.Matrix;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WeightMatrix {
    int rows, cols;
    int[][] countMatrix;
    int countSum = 1000000;

    public void initMatrix(int rows, int cols) {
        countMatrix = new int[rows][cols];
        setRows(rows);
        setCols(cols);
    }

    @Override
    public String toString() {
        return Arrays.asList(countMatrix)
                .stream()
                .map(b -> String.format("%d %d %d %d", b[0], b[1], b[2], b[3]))
                .collect(Collectors.joining("\n"));
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
