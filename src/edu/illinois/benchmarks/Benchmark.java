package edu.illinois.benchmarks;

import java.util.List;

public abstract class Benchmark {
    List<Integer> sites;
    
    public Benchmark(String outputDirectory) {
        //read sequence information

        //read predictions

    }

    /**
     * performs all benchmarks
     */
    public abstract void benchmark();

}
