package edu.illinois.tests;

import edu.illinois.Utils;
import edu.illinois.WeightMatrix;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by jwtrueb on 11/19/16.
 */
public class WeightMatrixTest {

    @Test
    public void initCountMatrixTest() {
        WeightMatrix wm = new WeightMatrix(1.5, 10);
        wm.initCountMatrix(10);
        System.out.println(wm);
    }

    @Test
    public void stochasticGradientDescentTest() {
        WeightMatrix wm = new WeightMatrix(1, 10);
        System.out.println(wm);
        wm = new WeightMatrix(1.5, 10);
        System.out.println(wm);
        wm = new WeightMatrix(2, 10);
        System.out.println(wm);
    }

}