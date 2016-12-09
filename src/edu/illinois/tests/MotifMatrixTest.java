package edu.illinois.tests;

import edu.illinois.Matrix.MotifMatrix;
import edu.illinois.Matrix.WeightMatrix;
import org.junit.Test;

import java.util.Random;

import static java.util.stream.IntStream.range;

/**
 * Created by jwtrueb on 11/19/16.
 */
public class MotifMatrixTest {

    @Test
    public void initCountMatrixTest() {
        MotifMatrix mm = new MotifMatrix(1.5, 10);
        mm.initCountMatrix();
        System.out.println(mm);
    }

    @Test
    public void stochasticGradientDescentTest() {
        MotifMatrix mm = new MotifMatrix(1, 10);
        System.out.println(mm);
        mm = new MotifMatrix(1.5, 10);
        System.out.println(mm);
        mm = new MotifMatrix(2, 10);
        System.out.println(mm);
    }

    @Test
    public void sampleTest() {
        Random r = new Random();
        MotifMatrix mm = new MotifMatrix(1, 10);
        System.out.println(mm);
        MotifMatrix finalmm = mm;
        range(0,10).forEach(i -> System.out.println(finalmm.sample(r)));

        mm = new MotifMatrix(1.5, 10);
        System.out.println(mm);
        MotifMatrix finalmm1 = mm;
        range(0,10).forEach(i -> System.out.println(finalmm1.sample(r)));

        mm = new MotifMatrix(2, 10);
        System.out.println(mm);
        MotifMatrix finalmm2 = mm;
        range(0,10).forEach(i -> System.out.println(finalmm2.sample(r)));

    }

}