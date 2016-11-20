package edu.illinois.tests;

import edu.illinois.Utils;
import edu.illinois.WeightMatrix;
import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;
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

    @Test
    public void sampleTest() {
        Random r = new Random();
        WeightMatrix wm = new WeightMatrix(1, 10);
        System.out.println(wm);
        WeightMatrix finalWm = wm;
        range(0,10).forEach(i -> System.out.println(finalWm.sample(r)));
        wm = new WeightMatrix(1.5, 10);
        System.out.println(wm);
        WeightMatrix finalWm1 = wm;
        range(0,10).forEach(i -> System.out.println(finalWm1.sample(r)));
        wm = new WeightMatrix(2, 10);
        System.out.println(wm);
        WeightMatrix finalWm2 = wm;
        range(0,10).forEach(i -> System.out.println(finalWm2.sample(r)));

    }

}